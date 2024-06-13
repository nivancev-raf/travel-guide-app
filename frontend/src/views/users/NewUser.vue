<template>
  <div class="new-user">
    <h2>Add New Users</h2>
    <form @submit.prevent="addUser">
      <div class="form-group">
        <label for="name">Name</label>
        <input type="text" v-model="newUser.firstName" class="form-control" id="name" required>
      </div>
      <div class="form-group">
        <label for="lastName">Last Name</label>
        <input type="text" v-model="newUser.lastName" class="form-control" id="lastName" required>
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" v-model="newUser.email" class="form-control" id="email" required>
      </div>
      <div class="form-group">
        <b-dropdown id="dropdown-1" :text="userTypeText" class="m-md-2 btn-primary" toggle-class="btn-primary">
          <b-dropdown-item @click="selectedType('ADMIN')">ADMIN</b-dropdown-item>
          <b-dropdown-item @click="selectedType('EDITOR')">EDITOR</b-dropdown-item>
        </b-dropdown>
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" v-model="newUser.hashedPassword" class="form-control" id="password" required>
      </div>
      <div class="form-group">
        <label for="confirmPassword">Confirm Password</label>
        <input type="password" v-model="confirmPassword" class="form-control" id="confirmPassword" required>
      </div>
      <div v-if="errorMessage" class="alert alert-danger alert-dismissible fade show custom-alert" role="alert">
        {{ errorMessage }}
        <button type="button" class="close" aria-label="Close" @click="resetFields">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <br>
      <button type="submit" class="btn btn-primary">Add User</button>
    </form>
  </div>
</template>

<script>

export default {
  data() {
    return {
      newUser: {
        firstName: '',
        lastName: '',
        email: '',
        hashedPassword: '',
        status: 'ACTIVE', 
        userType: ''
      },
      userType: 'Select User Type',
      confirmPassword: '',
      errorMessage: ''
    };
  },
  computed: {
    userTypeText() {
      return this.newUser.userType || 'Select User Type';
    }
  },
  methods: {

    addUser() {
      // if(this.user.status === 'EDITOR'){
      //   // editor cannot add new user
      //   this.errorMessage = 'Editor cannot add new user';
      //   return;
      // }

      if (!this.newUser.firstName || !this.newUser.lastName || !this.newUser.email || !this.newUser.hashedPassword || !this.confirmPassword || !this.newUser.userType) {
        this.errorMessage = 'All fields are required, including user type.';
        return;
      }

      if (this.newUser.hashedPassword !== this.confirmPassword) {
        this.errorMessage = 'Passwords do not match';
        return;
      }
      console.log(this.newUser);
      this.$axios.post('/api/users', this.newUser).then((response) => {
        if (response.data === "") {
          this.$router.push({ path: '/users', query: { error: 'User already exists' } });
        } else {
          this.$router.push('/users'); // Navigate back to users list
        }
      }).catch(error => {
        console.error("There was an error adding the user:", error);
        this.$router.push({ path: '/users', query: { error: 'Email address is already in use.' } });
      });
    },
    selectedType(type) {
      this.newUser.userType = type;
    },
    resetFields() {
      this.errorMessage = '';
      this.newUser.hashedPassword = '';
      this.confirmPassword = '';
    }
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
