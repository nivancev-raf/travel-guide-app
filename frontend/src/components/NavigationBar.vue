<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">RAF turisticki vodic</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <router-link to="/" tag="a" class="nav-link" :class="{active: $route.name === 'HomePage'}">Početna stranica</router-link>
            </li>
            <li class="nav-item">
              <router-link :to="{name: 'MostRead'}" tag="a" class="nav-link" :class="{active: $route.name === 'MostRead'}">Najčitanije</router-link>
            </li>
            <li class="nav-item">
              <router-link :to="{name: 'AllDestinations'}" tag="a" class="nav-link" :class="{active: $route.name === 'AllDestinations'}">Sve Destinacije</router-link>
            </li>
            <li class="nav-item">
              <router-link :to="{name: 'AllArticles'}" tag="a" class="nav-link" :class="{active: $route.name === 'AllArticles'}">Articles</router-link>
            </li>
            <li class="nav-item">
              <router-link :to="{name: 'AllUsers'}" tag="a" class="nav-link" :class="{active: $route.name === 'AllUsers'}">Users</router-link>
            </li>
          </ul>
          <div class="user-info" v-if="loggedIn">
            <span class="navbar-text me-2">Logged in user: {{ userName }}</span>
            <form class="d-flex" @submit.prevent="logout">
              <button class="btn btn-outline-secondary" type="submit">{{ logText }}</button>
            </form>
          </div>
          <div class="user-info" v-else>
            <router-link :to="{name: 'LoginPage'}" class="btn btn-outline-secondary">{{ logText }}</router-link>
          </div>
        </div>
      </div>
    </nav>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container-fluid">
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarDestinations" aria-controls="navbarDestinations" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarDestinations">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item" v-for="destination in destinations" :key="destination.id">
              <router-link :to="{name: 'GuestDestination', params: { destinationId: destination.id }}" tag="a" class="nav-link" :class="{active: $route.name === 'GuestDestination' && $route.params.destinationId == destination.id}">{{ destination.name }}</router-link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </div>
</template>

<script>
import {jwtDecode} from 'jwt-decode';

export default {
  name: "NavigationBar",
  data() {
    return {
      userName: '',
      destinations: [],
      loggedIn: false,
      logText: ''
    };
  },
  computed: {
    isLoggedIn() {
      return !!localStorage.getItem('jwt');
    }
  },
  methods: {
    checkAuth() {
      if (localStorage.getItem('jwt')) {
        this.fetchUserName();
        this.loggedIn = true;
        this.logText = 'Logout';
      } else {
        this.userName = '';
        this.loggedIn = false;
        this.logText = 'Login';
      }
    },
    logout() {
      localStorage.removeItem('jwt');
      this.checkAuth();
      this.$router.push({ name: 'LoginPage' });
    },
    fetchUserName() {
      const token = localStorage.getItem('jwt');
      if (token) {
        const decoded = jwtDecode(token);
        const userId = decoded.userId;
        this.$axios.get(`/api/users/${userId}`)
          .then(response => {
            this.userName = response.data.firstName + ' ' + response.data.lastName;
          })
          .catch(error => {
            console.error("There was an error fetching the user details:", error);
          });
      }
    },
    fetchDestinations() {
      this.$axios.get('/api/destinations')
        .then(response => {
          this.destinations = response.data;
        })
        .catch(error => {
          console.error("There was an error fetching the destinations:", error);
        });
    },
    handleDestinationAdded() {
      this.fetchDestinations();
    }
  },
  created() {
    this.checkAuth();
    this.fetchDestinations();
  },
  mounted() {
    this.$root.$on('destinationAdded', this.handleDestinationAdded);
  },
  beforeDestroy() { // beforeDestroy means before the component is destroyed to clean up
    this.$root.$off('destinationAdded', this.handleDestinationAdded);
  },
  watch: {
    $route() { // Watch for route changes
      this.checkAuth();
      this.handleDestinationAdded();
    }
  }
}
</script>

<style scoped>
.user-info {
  display: flex;
  align-items: center;
}
.navbar-text {
  margin-right: 10px;
}
.navbar-dark .navbar-nav .nav-link {
  color: rgba(255, 255, 255, 0.75);
}
.navbar-dark .navbar-nav .nav-link:hover,
.navbar-dark .navbar-nav .nav-link:focus {
  color: rgba(255, 255, 255, 1);
}
</style>
