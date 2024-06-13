<template>
  <div class="destinations">
    <h1 class="mt-4">Destinations</h1>
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
              <th scope="col">Name</th>
              <th scope="col">Description</th>
              <th scope="col">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="destination in destinations" :key="destination.id">
              <td scope="row">{{ destination.id }}</td>
              <td>
                <router-link :to="{ name: 'ArticlesByDestination', params: { destinationId: destination.id } }">
                  {{ destination.name }}
                </router-link>
              </td>
              <td>{{ destination.description }}</td>
              <td>
                <router-link :to="{ name: 'EditDestination', params: { id: destination.id } }"
                  class="btn btn-primary">Edit</router-link>
                <button @click="deleteDestination(destination.id)" class="btn btn-danger">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
        <router-link to="/destinations/new" class="btn btn-primary mt-3">Add New Destination</router-link>
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
export default {
  data() {
    return {
      destinations: [],
      errorMessage: '',
      articles: [],
      page: 1,
      limit: 10,
      hasMore: true
    };
  },
  created() {
    this.fetchDestinations();
    this.checkForError();
  },
  methods: {
    fetchDestinations() {
      this.$axios.get('/api/destinations', {
        params: {
          page: this.page,
          limit: this.limit
        }
      }).then((response) => {
        const destinations = response.data;
        this.hasMore = destinations.length === this.limit;
        this.destinations = destinations;
      }).catch(error => {
        console.error("There was an error fetching the destinations:", error);
      });
    },
    checkForError() {
      const error = this.$route.query.error;
      if (error) {
        this.errorMessage = error;
      }
    },
    fetchArticlesByDestination(destinationId) {
      return this.$axios.get(`/api/articles/destination/${destinationId}`).then((response) => {
        this.articles = response.data;
      }).catch(error => {
        console.error("There was an error fetching the articles:", error);
      });
    },
    deleteDestination(id) {
      this.fetchArticlesByDestination(id).then(() => {
        if (this.articles.length > 0) {
          this.errorMessage = 'Cannot delete destination with articles';
          return;
        }
        if (confirm('Are you sure you want to delete this destination?')) {
          this.$axios.delete(`/api/destinations/${id}`).then(() => {
            this.fetchDestinations();
          }).catch(error => {
            console.error("There was an error deleting the destination:", error);
          });
        }
      }).catch(error => {
        console.error("There was an error fetching the articles:", error);
      });
    },
    prevPage() {
      if (this.page > 1) {
        this.page--;
        this.fetchDestinations();
      }
    },
    nextPage() {
      if (this.hasMore) {
        this.page++;
        this.fetchDestinations();
      }
    }
  }
};
</script>

<style scoped>
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

.pagination {
  display: flex;
  align-items: center;
}

.pagination button {
  margin: 0 5px;
}
</style>
