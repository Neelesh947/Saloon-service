import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserResponseDTO } from '../../../services/DTOs/user-response-dto';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../services/user/user-service';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-edit-usermanagement-by-super-admin',
  imports: [FormsModule, CommonModule],
  templateUrl: './edit-usermanagement-by-super-admin.html',
  styleUrl: './edit-usermanagement-by-super-admin.scss',
})
export class EditUsermanagementBySuperAdmin implements OnInit {

  user: UserResponseDTO = {
    keycloakUserId: '',
    firstName: '',
    lastName: '',
    email: '',
    enable: true,
    bookedServiceIds: [],
    attributes: {
      phoneNumber: [''],
      address: ['']
    }
  };

  constructor(private route: ActivatedRoute, private router: Router, private user_service: UserService, private cdr: ChangeDetectorRef) { }


  ngOnInit(): void {
    const userId = this.route.snapshot.paramMap.get('id');
    if (userId) {
      this.loadUser(userId);
    }
  }

  loadUser(id: string) {
    this.user_service.getUserById(id).subscribe({
      next: (res: UserResponseDTO) => {
        const attributes = res.attributes ?? {};
        if (!Array.isArray(attributes['phoneNumber']) || attributes['phoneNumber'].length === 0) {
          attributes['phoneNumber'] = [''];
        }
        if (!Array.isArray(attributes['address']) || attributes['address'].length === 0) {
          attributes['address'] = [''];
        }
        this.user = {
          ...res,
          attributes
        };

        this.cdr.detectChanges();
      },
      error: (err) => {
        Swal.fire('Error', 'Something went wrong!', 'error');
      }
    });
  }

  get phoneNumber(): string {
    return this.user.attributes?.['phoneNumber']?.[0] || '';
  }

  set phoneNumber(value: string) {
    if (!this.user.attributes) this.user.attributes = {};
    this.user.attributes['phoneNumber'] = [value];
  }

  get address(): string {
    return this.user.attributes?.['address']?.[0] || '';
  }

  set address(value: string) {
    if (!this.user.attributes) this.user.attributes = {};
    this.user.attributes['address'] = [value];
  }

  saveUser() {
    if (!this.user.keycloakUserId) {
      Swal.fire('Error', 'User ID is missing!', 'error');
      return;
    }
    const payload = {
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      email: this.user.email,
      phone: this.user.attributes?.['phoneNumber']?.[0] || '',
      address: this.user.attributes?.['address']?.[0] || '',
      username: this.user.email,
      password: '',
      enabled: this.user.enable
    };
    this.user_service.updateUser(this.user.keycloakUserId, payload).subscribe({
      next: () => {
        Swal.fire('Success', 'User updated successfully!', 'success');
        this.router.navigate(['/user-management']);
      },
      error: () => {
        Swal.fire('Error', 'Failed to update user!', 'error');
      }
    })
  }
  cancel() {
    this.router.navigate(['super-admin-dashboard/user-management']);
  }
}
