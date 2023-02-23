import { Component, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Posted } from '../model';
import { PostService } from '../post.service';

@Component({
  selector: 'app-posted',
  templateUrl: './posted.component.html',
  styleUrls: ['./posted.component.css']
})
export class PostedComponent implements OnInit, OnDestroy {

  post !: Posted
  params$!: Subscription
  // form !: FormGroup

  constructor(private postSvc : PostService, private activatedRoute: ActivatedRoute, private fb : FormBuilder) {}

  ngOnInit(): void {
    this.params$ = this.activatedRoute.params.subscribe(
      (params) => {
        console.log("param val : " + params['postId']);
        const postId = params['postId']
        this.postSvc.getPost(postId)
          .then(result => {
            this.post = result
            console.info(">>>> PostedId in PostedComponent", result)
          })
          .catch((error) => {
            console.error(">>>> error: ", error)
          })
      }
    )
    // this.form = this.createForm(this.post)
  }

  ngOnDestroy(): void {
      this.params$.unsubscribe()
  }

  // createForm(post : Posted) : FormGroup {
  //   return this.fb.group({
  //       like: this.fb.control(post?.like ? post.like : ''),
  //       dislike: this.fb.control(post?.dislike ? post.dislike: '')
  //     })
  // }

  async like() {
    this.post = await this.postSvc.postLikeUpdate(this.post, "like")
    // this.form = this.createForm(this.post)
  }

  async dislike() {
    this.post = await this.postSvc.postLikeUpdate(this.post, "dislike")
  }
}
