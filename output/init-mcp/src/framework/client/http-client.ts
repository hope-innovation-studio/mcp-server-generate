
import axios, {type AxiosInstance, AxiosRequestConfig, AxiosResponse} from "axios";
import {type Config} from "../interface/system.interface";

/**
 * 使用单例模式生成发送请求的client
 */
export class HttpClient {

    /**
     * 单例
     * @private
     */
    private static instance:HttpClient;

    /**
     * 请求路径
     * @public
     */
    public baseUrl:string

    /**
     * 超时时间
     * @private
     */
    private timeout:number = 3000

    /**
     * 发送请求的实体
     * @private
     */
    private http:AxiosInstance

    /**
     * 配置
     */
    private config : Config


    /**
     * 这个应该让用户来写
     * 这里只是写一个事例
     * @private
     */
    private constructor(config:Config) {
        this.config = config;
        const httpConfig = config.httpConfig
        this.timeout = httpConfig.timeout != null ? httpConfig.timeout : this.timeout
        this.baseUrl = httpConfig.baseUrl
        this.http = axios.create({
            baseURL: this.baseUrl,
            timeout: this.timeout,
        });

        // 请求拦截器：自动注入 MES 自定义 token 头
        this.http.interceptors.request.use(
            async (reqConfig) => {
                //用户自己填写逻辑
            },
            (error) => Promise.reject(error)
        );
    }


    public static getInstance(config?:Config): HttpClient {
        if (!HttpClient.instance) {
            if (!config) {
                throw new Error("首次初始化单例时，必须传入 baseURL 参数！");
            }
            HttpClient.instance = new HttpClient(config);
        }
        return HttpClient.instance;
    }

// -------------------  统一的通用请求方法 -------------------

    /**
     * 通用 GET 请求
     * @param url 请求路径
     * @param config Axios 任意配置（包含 params, headers, timeout 等）
     */
    public async get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
        const response = await this.http.get<T>(url, config);
        return response.data;
    }

    /**
     * 通用 POST 请求
     * @param url 请求路径
     * @param data 请求体 Body (JSON / FormData / Params)
     * @param config Axios 任意配置
     */
    public async post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
        const response = await this.http.post<T>(url, data, config);
        return response.data;
    }



    /**
     * 通用 HTTP 请求，返回完整 Axios 响应
     */
    public request<TResponse = unknown, TBody = unknown>(
        config: AxiosRequestConfig<TBody>,
    ): Promise<AxiosResponse<TResponse, TBody>> {
        return this.http.request<
            TResponse,
            AxiosResponse<TResponse, TBody>,
            TBody
        >(config);
    }

    /**
     * 通用 HTTP 请求，只返回响应数据
     */
    public async requestData<TResponse = unknown, TBody = unknown>(
        config: AxiosRequestConfig<TBody>,
    ): Promise<TResponse> {
        const response = await this.request<TResponse, TBody>(config);
        return response.data;
    }

}
