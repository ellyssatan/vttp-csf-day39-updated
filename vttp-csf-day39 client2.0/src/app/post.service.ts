import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { Post, Posted, PostResponse } from "./model";

@Injectable()
export class PostService {

    constructor(private http : HttpClient) {}
    // BACKEND_API_URL = 'https://day39-server-production.up.railway.app'
    BACKEND_API_URL = 'http://localhost:8080'

    postComment(post : Post) : Promise<PostResponse> {

        const form = new FormData()
        form.set("email", post.email)
        form.set("title", post.title)
        form.set("text", post.text)
        form.set("image", post.image)

        return firstValueFrom(this.http.post<PostResponse>(`${this.BACKEND_API_URL}/post`, form))
    }

    getPost(postId : string) : Promise<Posted> {

        console.info(".... posting..... to ", `${this.BACKEND_API_URL}/post/${postId}`)

        return firstValueFrom<Posted>(
            this.http.get<Posted>(`${this.BACKEND_API_URL}/post/${postId}`)
        )
    }

    postLikeUpdate(post : Posted, vote : string) : Promise<Posted> {

        return firstValueFrom(this.http.post<Posted>(`${this.BACKEND_API_URL}/post/${post.postId}/${vote}`, vote))
    }

}

