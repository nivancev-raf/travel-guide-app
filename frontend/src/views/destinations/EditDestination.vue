<template>
    <div class="edit-destination">
      <h2>Edit Destination</h2>
      <form @submit.prevent="editDestination">
        <div class="form-group">
          <label for="name">Name</label>
          <input type="text" v-model="destination.name" class="form-control" id="name" required>
        </div>
        <div class="form-group">
          <label for="description">Description</label>
          <input type="text" v-model="destination.description" class="form-control" id="description" required>
        </div>
        <br>
        <button type="submit" class="btn btn-primary">Submit</button>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        destination: {
          name: '',
          description: ''
        }
      };
    },
    methods: {
      fetchDestination() {
        this.$axios.get(`/api/destinations/${this.$route.params.id}`).then((response) => {
          this.destination = response.data;
        }).catch(error => {
          console.error("There was an error fetching the destination:", error);
        });
      },
        editDestination() {
            this.$axios.put(`/api/destinations/${this.$route.params.id}`, this.destination).then((response) => {
            console.log("Destination edited successfully:", response.data);
            this.$router.push('/destinations'); // Navigate back to destinations list
            }).catch(error => {
            console.error("There was an error editing the destination:", error);
            });
        }
    },
    created() {
      this.fetchDestination();
    }
  };
  </script>
  
  <style scoped>
  /* Add any required styling here */
  </style>
  