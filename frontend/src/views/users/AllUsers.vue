<template>
  <div class="users">
    <h1 class="mt-4">All Users</h1>
    <div v-if="errorMessage" class="alert alert-danger alert-dismissible fade show custom-alert" role="alert">
      {{ errorMessage }}
      <button type="button" class="close" aria-label="Close" @click="errorMessage = ''">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="row">
      <div class="col-12">
        <table class="table">
          <thead>
            <tr>
              <th scope="col">ID</th>
              <th scope="col">First Name</th>
              <th scope="col">Last Name</th>
              <th scope="col">Email</th>
              <th scope="col">User Type</th>
              <th scope="col">Status</th>
              <th scope="col">Edit Action</th>
              <th scope="col">Activation Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td scope="row">{{ user.id }}</td>
              <td>{{ user.firstName }}</td>
              <td>{{ user.lastName }}</td>
              <td>{{ user.email }}</td>
              <td>{{ user.userType }}</td>
              <td>{{ user.status }}</td>
              <td>
                <router-link :to="{ name: 'EditUser', params: { id: user.id } }" class="btn btn-primary">Edit</router-link>
              </td>
              <td v-if="user.userType === 'EDITOR'">
                <button v-if="user.status === 'INACTIVE'" @click="activateUser(user.id)" class="btn btn-primary">Activate</button>
                <button v-else @click="deactivateUser(user.id)" class="btn btn-danger">Deactivate</button>
              </td>
              <td v-else>Admin is always active</td>
            </tr>
          </tbody>
        </table>
        <router-link to="/usersnew/new" class="btn btn-primary mt-3">Add New User</router-link>
        <div class="pagination mt-3">
          <button @click="prevPage" :disabled="page === 1" class="btn btn-secondary">Previous</button>
          <span class="mx-2">Page {{ page }}</span>
          <button @click="nextPage" :disabled="!hasMore" class="btn btn-secondary">Next</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { jwtDecode } from 'jwt-decode';
export default {
  data() {
    return {
      users: [],
      user: {},
      errorMessage: '',
      page: 1,
      limit: 10,
      hasMore: true
    };
  },
  created() {
    this.fetchUsers();
    this.fetchLoggedInUserById();
    this.checkForError();
  },
  methods: {
    fetchUsers() {
      this.$axios.get('/api/users', {
        params: {
          page: this.page,
          limit: this.limit
        }
      }).then((response) => {
        const users = response.data;
        this.hasMore = users.length === this.limit;
        this.users = users;
      }).catch(error => {
        console.error("There was an error fetching the users:", error);
        this.errorMessage = 'There was an error fetching the users. Please try again.';
      });
    },
    getAuthorIdFromToken() {
      const token = localStorage.getItem('jwt');
      if (token) {
        const decoded = jwtDecode(token);
        return decoded.userId;
      }
      return null;
    },
    fetchLoggedInUserById() {
      this.user.userId = this.getAuthorIdFromToken();
      this.$axios.get(`/api/users/${this.user.userId}`).then((response) => {
        this.user = response.data;
      }).catch(error => {
        console.error("There was an error fetching the user:", error);
      });
    },
    activateUser(userId) {
      this.updateUserStatus(userId, 'ACTIVE');
    },
    deactivateUser(userId) {
      this.updateUserStatus(userId, 'INACTIVE');
    },
    updateUserStatus(userId, status) {
      this.$axios.put(`/api/users/${userId}/status`, { status }).then(() => {
        this.fetchUsers();
      }).catch(error => {
        console.error(`There was an error updating the user status:`, error);
        this.errorMessage = 'There was an error updating the user status. Please try again.';
      });
    },
    checkForError() {
      const error = this.$route.query.error;
      if (error) {
        this.errorMessage = error;
      }
    },
    prevPage() {
      if (this.page > 1) {
        this.page--;
        this.fetchUsers();
      }
    },
    nextPage() {
      if (this.hasMore) {
        this.page++;
        this.fetchUsers();
      }
    }
  }
};
</script>

<style scoped>
.custom-alert {
  max-width: 330px;
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

.table {
  width: 100%;
  margin: 20px 0;
}

.pagination {
  display: flex;
  align-items: center;
}

.pagination button {
  margin: 0 5px;
}
</style>
