<template>
    <div class="new-destination">
      <h2>Add New Destination</h2>
      <form @submit.prevent="addDestination">
        <div class="form-group">
          <label for="name">Name</label>
          <input type="text" v-model="newDestination.name" class="form-control" id="name" required>
        </div>
        <div class="form-group">
          <label for="description">Description</label>
          <input type="text" v-model="newDestination.description" class="form-control" id="description" required>
        </div>
        <br>
        <button type="submit" class="btn btn-primary">Add Destination</button>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        newDestination: {
          name: '',
          description: ''
        }
      };
    },
    methods: {
      addDestination() {
        this.$axios.post('/api/destinations', this.newDestination).then((response) => {
          if (response.data === "") {
            this.$router.push({ path: '/destinations', query: { error: 'Destination name already exists' } });
          } else {
            this.$emit('destinationAdded'); // Emit an event
            this.$router.push('/destinations'); // Navigate back to destinations list
          }
        }).catch(error => {
          console.error("There was an error adding the destination:", error);
          this.$router.push({ path: '/destinations', query: { error: 'There was an error adding the destination. Please try again.' } });
        });
      }
    }
  };
  </script>
  
  <style scoped>
  /* Add any required styling here */
  </style>
  