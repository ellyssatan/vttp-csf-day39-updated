import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Post } from '../model';
import { PostService } from '../post.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  @ViewChild('image')
  image !: ElementRef

  form !: FormGroup

  constructor(private fb : FormBuilder, private postSvc : PostService, private router : Router) {}

  ngOnInit(): void {
    this.form = this.createForm()
  }

  process() {
    //get form value
    const data = this.form.value as Post
    // get image
    data.image = this.image.nativeElement.files[0]

    console.info('>>> post: ', data)

    // send to svc to post to SB
    this.postSvc.postComment(data)
      .then(response => {
        console.info(`Post id: ${response.postId}`)   // get the postId back from SB
        this.form = this.createForm()                 // reset form
        // this.postSvc.getPost(response.postId);
        this.router.navigate(['/posted', response.postId])
      }).catch(err => {
        console.error(">>>>> error posting to 8080", err)
      })
  }

  createForm() : FormGroup {
    return this.fb.group({
      email : this.fb.control('', [Validators.required, Validators.email]),
      title : this.fb.control('', [Validators.required]),
      text : this.fb.control('', [Validators.required, Validators.minLength(5)]),
      image : this.fb.control('', [Validators.required])
    })
  }
}
