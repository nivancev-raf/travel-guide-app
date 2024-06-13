<template>
    <div class="edit-user">
      <h2>Edit User</h2>
      <form @submit.prevent="editUser">
        <div class="form-group">
          <label for="name">Name</label>
          <input type="text" v-model="user.firstName" class="form-control" id="name" required>
        </div>
        <div class="form-group">
          <label for="lastName">Last Name</label>
          <input type="text" v-model="user.lastName" class="form-control" id="lastName" required>
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" v-model="user.email" class="form-control" id="email" required>
        </div>
        <label>Currently type: {{ user.userType }}</label>
        <div class="form-group">
          <b-dropdown id="dropdown-1" :text="userTypeText" class="m-md-2 btn-primary" toggle-class="btn-primary">
            <b-dropdown-item @click="selectType('ADMIN')">ADMIN</b-dropdown-item>
            <b-dropdown-item @click="selectType('EDITOR')">EDITOR</b-dropdown-item>
          </b-dropdown>
        </div>
        <div v-if="errorMessage" class="alert alert-danger alert-dismissible fade show custom-alert" role="alert">
          {{ errorMessage }}
          <button type="button" class="close" aria-label="Close" @click="resetFields">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <br>
        <button type="submit" class="btn btn-primary">Edit User</button>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        user: {
          firstName: '',
          lastName: '',
          email: '',
          hashedPassword: '',
          status: '', 
          userType: ''
        },
        errorMessage: ''
      };
    },
    computed: {
      userTypeText() {
        return this.user.userType || 'Select User Type';
      }
    },
    methods: {
      fetchUserById() {
        this.$axios.get(`/api/users/${this.$route.params.id}`).then((response) => {
          this.user = response.data;
        }).catch(error => {
          console.error("There was an error fetching the user:", error);
        });
      },
      editUser() {
        if (!this.user.firstName || !this.user.lastName || !this.user.email || !this.user.userType) {
          this.errorMessage = 'All fields are required, including user type.';
          return;
        }
  
        this.$axios.put(`/api/users/${this.user.id}`, this.user).then(() => {
          this.$router.push('/users'); // Navigate back to users list
        }).catch(error => {
          console.error("There was an error editing the user:", error);
          this.$router.push({ path: '/users', query: { error: 'There was an error editing the user. Please try again.' } });
        });
      },
      selectType(type) {
        this.user.userType = type;
      },
      resetFields() {
        this.errorMessage = '';
        this.user.hashedPassword = '';
        this.user.confirmPassword = '';
      }
    },
    created() {
      this.fetchUserById();
    }
  };
  </script>
  
  <style scoped>
  .form-group {
    margin-bottom: 1rem;
    max-width: 400px;
  }
  
  .custom-alert {
    max-width: 380px;
    margin: 0 0 20px 0;
  }
  
  .close {
    background: none;
    border: none;
    font-size: 1.5rem;
    line-height: 1;
    color: #000;
    opacity: 0.5;
  }
  
  .close:hover {
    color: #000;
    opacity: 0.75;
  }
  
  .close:focus {
    outline: none;
    box-shadow: none;
  }
  </style>
  