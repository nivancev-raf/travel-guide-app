<template>
  <div class="articles">
    <h2 class="mt-4">Articles By Destination: {{ destinationName }} </h2>
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
              <th scope="col">Title</th>
              <th scope="col">Author Name</th>
              <th scope="col">Date Created</th>
              <th scope="col">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="article in articles" :key="article.id">
              <td scope="row">{{ article.id }}</td>
              <td>{{ article.title }}</td>
              <td>{{ article.authorName }}</td>
              <td>{{ new Date(article.creationTime).toLocaleString() }}</td>
              <td>
                <router-link :to="{name: 'EditArticle', params: {id: article.id}}" class="btn btn-primary">Edit</router-link>
                <button @click="deleteArticle(article.id)" class="btn btn-danger">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
        <router-link :to="{name: 'NewArticle', query: { destinationId: destinationId }}" class="btn btn-primary mt-3">Add New Article</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      destinationId: null,
      articles: [],
      errorMessage: '',
      destinationName: ''
    };
  },
  created() {
    this.destinationId = this.$route.params.destinationId;
    this.fetchArticlesByDestinationID();
    this.fetchDestinationById();
    this.checkForError();
  },
  methods: {
    fetchArticlesByDestinationID() {
      this.$axios.get(`/api/articles/destination/${this.destinationId}`).then((response) => {
        const articles = response.data;
        // Fetch author names for each article
        const fetchPromises = articles.map(article => {
          return this.$axios.get(`/api/users/${article.authorId}`).then(userResponse => {
            article.authorName = userResponse.data.firstName;
            return article;
          });
        });
        Promise.all(fetchPromises).then(updatedArticles => {
          this.articles = updatedArticles;
        });
      }).catch(error => {
        console.error("There was an error fetching the articles:", error);
        this.errorMessage = 'There was an error fetching the articles. Please try again.';
      });
    },
    fetchDestinationById() {
      this.$axios.get(`/api/destinations/${this.destinationId}`).then((response) => {
        this.destinationName = response.data.name;
      }).catch(error => {
        console.error("There was an error fetching the destination:", error);
        this.errorMessage = 'There was an error fetching the destination. Please try again.';
      });
    },
    checkForError() {
      const error = this.$route.query.error;
      if (error) {
        this.errorMessage = error;
      }
    },
    deleteArticle(articleId) {
      console.log("Deleting article with ID:", articleId);
      // Add delete logic here
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
</style>
