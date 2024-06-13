<template>
  <div class="container">
    <h1>Articles for {{ destinationName }}</h1>
    <ul class="list-group">
      <li v-for="article in articles" :key="article.id" class="list-group-item">
        <h2>
          <router-link :to="{ name: 'ArticleDetails', params: { id: article.id } }">
            {{ article.title }}
          </router-link>
        </h2>
        <p>{{ article.text.substring(0, 100) }}...</p>
        <small>Published on: {{ new Date(article.creationTime).toLocaleDateString() }}</small>
      </li>
    </ul>
  </div>
</template>
  
  <script>
  export default {
    data() {
      return {
        articles: [],
        destinationName: ''
      };
    },
    created() {
      this.fetchArticles();
      this.fetchDestinationById(this.$route.params.destinationId);
    },
    methods: {
      handleRouteChange(newId) {
          this.destinationId = newId;
          this.fetchArticles();
          this.fetchDestinationById(this.$route.params.destinationId);
      },
      fetchArticles() {
        const destinationId = this.$route.params.destinationId;
        this.$axios.get(`/api/articles/destination/${destinationId}`)
          .then(response => {
            this.articles = response.data;
          })
          .catch(error => {
            console.error("There was an error fetching the articles:", error);
          });
      },
      // fetch destination name by id
        fetchDestinationById(id) {
            this.$axios.get(`/api/destinations/${id}`).then((response) => {
            this.destinationName = response.data.name;
            }).catch(error => {
            console.error("There was an error fetching the destination:", error);
            });
        }
    },
    watch: {
      '$route.params.destinationId': 'handleRouteChange'
    },
  }
  </script>
  
  <style scoped>
  .container {
    margin-top: 20px;
  }
  .list-group-item {
    margin-bottom: 10px;
  }
  </style>
  