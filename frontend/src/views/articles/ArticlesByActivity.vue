<template>
    <div class="articles-by-activity">
      <h1>Articles about {{ activityName }}</h1>
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
                <th scope="col">Text</th>
                <th scope="col">Destination</th>
                <th scope="col">Date Created</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="article in articles" :key="article.id">
                <td scope="row">{{ article.id }}</td>
                <td>
                  <router-link :to="{ name: 'ArticleDetails', params: { id: article.id } }">
                    {{ article.title }}
                  </router-link>
                </td>
                <td>{{ article.text.substring(0, 100) }}...</td>
                <td>{{ destinationName }}</td>
                <td>{{ new Date(article.creationTime).toLocaleDateString() }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        articles: [],
        activityName: '',
        destinationName: '',
        errorMessage: ''
      };
    },
    methods: {
      fetchArticlesByActivity() {
        const activityId = this.$route.params.activityId;
        this.$axios.get(`/api/articles/activity/${activityId}`).then((response) => {
          console.log(response.data);
          this.articles = response.data;
          this.fetchActivityName();
          if (this.articles.length > 0) {
            this.fetchDestinationById(this.articles[0].destinationId);
          }
        }).catch(error => {
          console.error("There was an error fetching the articles:", error);
          this.errorMessage = 'There was an error fetching the articles. Please try again.';
        });
      },
      fetchActivityName() {
        const activityId = this.$route.params.activityId;
        this.$axios.get(`/api/activities/${activityId}`).then((response) => {
          this.activityName = response.data.name;
        }).catch(error => {
          console.error("There was an error fetching the activity name:", error);
          this.errorMessage = 'There was an error fetching the activity name. Please try again.';
        });
      },
      fetchDestinationById(id) {
        this.$axios.get(`/api/destinations/${id}`).then((response) => {
          this.destinationName = response.data.name;
        }).catch(error => {
          console.error("There was an error fetching the destination:", error);
          this.errorMessage = 'There was an error fetching the destination. Please try again.';
        });
      }
    },
    created() {
      this.fetchArticlesByActivity();
    }
  };
  </script>
  
  <style scoped>
  .articles-by-activity {
    max-width: 800px;
    margin: 0 auto;
  }
  
  .table {
    width: 100%;
    margin: 20px 0;
  }
  
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
  