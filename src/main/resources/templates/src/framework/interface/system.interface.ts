export interface Config{
    fileConfig:{
        toolsDir:string,
        promptsDir?:string,
        resourcesDir?:string
    },
    httpConfig:{
        baseUrl:string,
        timeout?:number
        accessToken:string
    }
}