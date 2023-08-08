import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageUploaderService {

  constructor(private http: HttpClient) { }

  uploadImage(image: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', image);
    return this.http.post('http://localhost:8080/api/image', formData, { reportProgress: true, observe: 'events'});
  }

  getImage(imageName: string): Observable<any> {
    return this.http.get('http://localhost:8080/api/image/' + imageName);
  }

}
