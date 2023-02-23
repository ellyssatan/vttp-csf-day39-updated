import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { MainComponent } from './components/main.component';
import { PostService } from './post.service';
import { PostedComponent } from './components/posted.component';
import { RouterModule, Routes } from '@angular/router';

const appRoutes : Routes = [
	{ path: '', component: MainComponent},
	{ path: 'posted/:postId', component: PostedComponent },
	{ path: '**', redirectTo: '/', pathMatch: 'full' }
]

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    PostedComponent
  ],
  imports: [
    BrowserModule, ReactiveFormsModule, HttpClientModule, RouterModule.forRoot(appRoutes)
  ],
  providers: [ PostService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
