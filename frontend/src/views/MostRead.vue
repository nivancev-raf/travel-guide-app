<template>
    <div class="most-read container">
      <h1>Most Read Articles</h1>
      <div v-if="errorMessage" class="alert alert-danger alert-dismissible fade show custom-alert" role="alert">
        {{ errorMessage }}
        <button type="button" class="close" aria-label="Close" @click="errorMessage = ''">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <ul class="list-group">
        <li v-for="article in articles" :key="article.id" class="list-group-item">
          <!-- <h2>{{ article.title }}</h2> -->
          <h2>
            <router-link :to="{ name: 'ArticleDetails', params: { id: article.id } }">
                {{ article.title }}
            </router-link>
          </h2>
          <p>{{ article.text.substring(0, 100) }}...</p>
          <p><strong>Destination:</strong> {{ article.destinationName }}</p>
          <p><strong>Date:</strong> {{ new Date(article.creationTime).toLocaleDateString() }}</p>
          <p><strong>Visit Count:</strong> {{ article.visitCount }}</p>
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  export default {
    name: 'MostRead',
    data() {
      return {
        articles: [],
        errorMessage: ''
      };
    },
    created() {
      this.fetchMostReadArticles();
    },
    methods: {
        fetchMostReadArticles() {
            const thirtyDaysAgo = new Date();
            thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
            this.$axios.get(`/api/articles/mostRead`, {
                params: {
                limit: 10,
                sort: 'visit_count,desc',
                filter: `creation_time,gt,${thirtyDaysAgo.toISOString()}`
                }
            }).then((response) => {
                const articles = response.data;

                // Fetch destination names for each article
                const fetchPromises = articles.map(article => {
                return this.$axios.get(`/api/destinations/${article.destinationId}`).then(destinationResponse => {
                    article.destinationName = destinationResponse.data.name;
                    return article;
                });
                });

                Promise.all(fetchPromises).then(updatedArticles => {
                this.articles = updatedArticles;
                });

            }).catch(error => {
                console.error("There was an error fetching the articles:", error);
            });
        }
    }
  };
  </script>
  
  <style scoped>
  .most-read {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 20px;
  }
  
  .list-group {
    margin-top: 20px;
  }
  
  .list-group-item {
    margin-bottom: 20px;
  }
  
  .custom-alert {
    max-width: 330px;
    margin: 20px auto;
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
  