<template>
  <div class="pt-5">  <!-- padding top 5 -->
    <h1 v-if="email">Hello, {{email}}</h1>
    <form @submit.prevent="login">
      <div class="form-group">
        <label for="email">Username</label>
        <input v-model="email" type="text" class="form-control" id="email" aria-describedby="usernameHelp" placeholder="Enter email">
        <small id="usernameHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
      </div>
      <div class="form-group">
        <label for="exampleInputPassword1">Password</label>
        <input v-model="password" type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
      </div>
      <button type="submit" class="btn btn-primary mt-2">Submit</button>  <!-- mt = margin top 2 -->
    </form>
  </div>
</template>

<script>
export default {
  name: "LoginPage",
  data() {
    return {
      email: '',
      password: '',
    }
  },
  methods: {
    login() {
      this.$axios.post('/api/users/login', {
        email: this.email,
        password: this.password,
      }).then(response => {
        localStorage.setItem('jwt', response.data.jwt)
        this.$router.push({name: 'AllDestinations'});
      }).catch(error => {
        console.error(error);
      });
    }
  },
}
</script>

<style scoped>

</style>
