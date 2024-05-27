<template>
  <div class="destinations">
    <h1 class="mt-4">Destinations</h1>

    <div class="row">
      <div class="col-4">
        <table class="table">
          <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Title</th>
            <th scope="col">Content</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="destination in destinations" :key="destination.id">
            <th scope="row">{{ destination.id }}</th>
            <td>{{ destination.title | capitalize }}</td>
            <td>{{ destination.content | shortText }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  filters: {
    shortText(value) {
      if (value.length < 30) {
        return value;
      }
      return value.slice(0, 30) + '...'
    }
  },
  data() {
    return {
      destinations: []
    }
  },
  created() {
    // this.$axios.get('/api/subjects').then((response) => {
    //   this.subjects = response.data;
    // });
    this.destinations = [
      {id: 1, title: 'Destination 1', content: 'Content 1'},
      {id: 2, title: 'Destination 2', content: 'Content 2'},
      {id: 3, title: 'Destination 3', content: 'Content 3'},
      {id: 4, title: 'Destination 4', content: 'Content 4'},
      {id: 5, title: 'Destination 5', content: 'Content 5'},
    ]
  },
}
</script>
