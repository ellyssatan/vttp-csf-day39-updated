export interface Post {
    email : string
    title : string
    text : string
    image : File
}

export interface PostResponse {
    postId : string
}

export interface Posted {
    postId : string
	postDate : Date
    userId : number
    email : string
    name : string
    title : string
    text : string
    imageUrl : string
    like : number
    dislike : number

}