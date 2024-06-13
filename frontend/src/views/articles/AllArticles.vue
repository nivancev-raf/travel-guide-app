<template>
  <div class="articles">
    <h1 class="mt-4">Articles</h1>
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
            <tr v-for="article in sortedArticles" :key="article.id">
              <td scope="row">{{ article.id }}</td>
              <td>
                <router-link :to="{ name: 'ArticleDetails', params: { id: article.id } }">
                  {{ article.title }}
                </router-link>
              </td>
              <td>{{ article.authorName }}</td>
              <td>{{ new Date(article.creationTime).toLocaleString() }}</td>
              <td>
                <router-link :to="{ name: 'EditArticle', params: { id: article.id } }" class="btn btn-primary">Edit</router-link>
                <button @click="deleteArticle(article.id)" class="btn btn-danger">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
        <router-link to="/articles/new" class="btn btn-primary mt-3">Add New Article</router-link>
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
      articles: [],
      errorMessage: '',
      page: 1,
      limit: 10,
      hasMore: true
    };
  },
  created() {
    this.fetchArticles();
    this.checkForError();
  },
  methods: {
    fetchArticles() {
      this.$axios.get('/api/articles', {
        params: {
          page: this.page,
          limit: this.limit
        }
      }).then((response) => {
        const articles = response.data;
        this.hasMore = articles.length === this.limit; // If we get less than the limit, there are no more articles
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
      // first - delete the article from articleActivity table
      this.$axios.delete(`/api/activities/article/${articleId}`).then(() => {
        // then - delete the article
        this.$axios.delete(`/api/articles/${articleId}`).then(() => {
          this.fetchArticles();
        }).catch(error => {
          console.error("There was an error deleting the article:", error);
        });
      }).catch(error => {
        console.error("There was an error deleting the article activity:", error);
      });

      // delete comments for that article
      this.$axios.delete(`/api/comments/article/${articleId}`).then(() => {
        console.log("Comments deleted");
      }).catch(error => {
        console.error("There was an error deleting the comments:", error);
      });
      

    },
    prevPage() {
      if (this.page > 1) {
        this.page--;
        this.fetchArticles();
      }
    },
    nextPage() {
      if (this.hasMore) {
        this.page++;
        this.fetchArticles();
      }
    }
  },
  computed: {
    sortedArticles() {
      return [...this.articles].sort((a, b) => new Date(b.creationTime) - new Date(a.creationTime));
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

.pagination {
  display: flex;
  align-items: center;
}

.pagination button {
  margin: 0 5px;
}
</style>
