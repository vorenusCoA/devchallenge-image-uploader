import { Component, OnInit } from '@angular/core';
import { ImageUploaderService } from '../image-uploader.service';
import { HttpEventType } from '@angular/common/http';

import { Clipboard } from '@angular/cdk/clipboard';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  showInitContainer = true;
  showUploadingContainer = false;
  showConfirmationContainer = false;
  isDragOver = false;
  uploadProgress?: number;
  uploadedImageLocation = "https://localhost:8080/api/images/asdasdsadasdas";

  constructor(private imageUploaderService: ImageUploaderService, private clipboard: Clipboard, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver = true;
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver = false;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver = false;
    const file = event.dataTransfer?.files[0];
    this.handleFileUpload(file);
  }

  onFileChoosen(event: any): void {
    const file = event.target.files[0];
    this.handleFileUpload(file);
  }

  handleFileUpload(file: File | undefined | null): void {
    if (file && file.type.startsWith('image/')) {

      if (this.getSizeInKB(file.size) > 500) {
        this.openSnackBar('Max limit (500 KB) exceeded!');
        return;
      }

      this.showInitContainer = false;
      this.showUploadingContainer = true;

      this.imageUploaderService.uploadImage(file).subscribe({
        next: (event) => {
          if (event.type == HttpEventType.UploadProgress) {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          } else if (event.type == HttpEventType.Response) {
            console.log('event: ', event);
            this.uploadedImageLocation = event.body.fileLocation;
          }

        },
        complete: () => {
          this.showUploadingContainer = false;
          this.showConfirmationContainer = true;
        }
      })

    } else {
      this.openSnackBar('Invalid file type!');
    }
  }

  getSizeInKB(size: number): number {
    return Math.round(size / 1000);
  }

  openSnackBar(message: string): void {
    this.snackBar.open(message, 'Close', {duration: 5000, verticalPosition: 'top', horizontalPosition: 'center'});
  }

  copyToClipboard() {
    this.clipboard.copy(this.uploadedImageLocation);
  }

}
