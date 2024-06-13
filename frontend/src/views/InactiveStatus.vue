<template>
    <div class="inactive-status">
      <h1>User with 'EDITOR' user type must not have the ability to create, list and modify user data.</h1>
      <p>Please return to the home page.</p>
      <button @click="goHome" class="btn btn-primary">Home Page</button>
    </div>
  </template>
  
  <script>
  export default {
    methods: {
      goHome() {
        this.$router.push('/');
      }
    }
  };
  </script>
  
  <style scoped>
  .inactive-status {
    text-align: center;
    margin-top: 50px;
  }
  
  .inactive-status h1 {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  .inactive-status p {
    font-size: 18px;
    margin-bottom: 20px;
  }
  
  .inactive-status button {
    font-size: 16px;
    padding: 10px 20px;
  }
  </style>
  