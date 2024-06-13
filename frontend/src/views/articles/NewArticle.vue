<template>
  <div class="new-article">
    <h2>Add New Article</h2>
    <form @submit.prevent="submitArticle">
      <div class="form-group">
        <label for="title">Title</label>
        <input type="text" v-model="newArticle.title" class="form-control" id="title" required>
      </div>
      <div v-if="!destinationId">
        <div class="form-group">
          <b-dropdown id="dropdown-1" :text="selectedDestination ? selectedDestination.name : 'Select Destination'" class="m-md-2 btn-primary" toggle-class="btn-primary">
            <b-dropdown-item v-for="destination in destinations" :key="destination.id" @click="selectDestination(destination)">
              {{ destination.name }}
            </b-dropdown-item>
          </b-dropdown>
        </div>
      </div>
      <div v-else>
        <div class="form-group">
          <label for="destination">Destination</label>
          <input type="text" v-model="selectedDestination.name" class="form-control" id="destination" disabled>
        </div>
      </div>
      <div class="form-group">
        <label for="text">Text</label>
        <textarea v-model="newArticle.text" class="form-control" id="text" required></textarea>
      </div>
      <div class="form-group">
        <b-dropdown id="dropdown-2" :text="dropdownText" class="m-md-2 btn-primary" toggle-class="btn-primary">
          <b-dropdown-item v-for="activity in addedActivities" :key="activity.id" @click="selectActivity(activity)">
            {{ activity.name }}
          </b-dropdown-item>
        </b-dropdown>
      </div>
      <div class="form-group">
        <label for="newActivity">New Activity</label>
        <input type="text" v-model="newActivity.name" class="form-control" id="newActivity">
      </div>
      <div class="form-group">
        <button type="button" class="btn btn-secondary" @click="addActivity">Add New Activity</button>
      </div>
      <div class="form-group">
        <button type="submit" class="btn btn-primary">Add Article</button>
      </div>
    </form>
  </div>
</template>

<script>
import {jwtDecode} from 'jwt-decode';

export default {
  props: {
    destinationId: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      newArticle: {
        title: '',
        text: '',
        destinationId: this.destinationId,
        authorId: null,
        creationTime: new Date(),
        visitCount: 0
      },
      newActivity: {
        name: ''
      },
      destinations: [],
      addedActivities: [],
      selectedDestination: null,
      selectedActivity: null
    };
  },
  computed: {
    dropdownText() {
      if (this.addedActivities && this.addedActivities.length > 0) {
        return this.selectedActivity ? this.selectedActivity.name : 'Select Activity';
      } else {
        return 'No activities';
      }
    }
  },
  methods: {
    getAuthorIdFromToken() {
      const token = localStorage.getItem('jwt');
      if (token) {
        const decoded = jwtDecode(token);
        return decoded.userId;
      }
      return null;
    },
    addArticle() {
      this.newArticle.authorId = this.getAuthorIdFromToken();
      if (this.selectedDestination) {
        this.newArticle.destinationId = this.selectedDestination.id;
      }
      return this.$axios.post('/api/articles', this.newArticle).then((response) => {
        if (response.data === "") {
          this.$router.push({ path: '/articles', query: { error: 'Article name already exists' } });
          return null;
        } else {
          return response.data;
        }
      }).catch(error => {
        console.error("There was an error adding the article:", error);
        this.$router.push({ path: '/articles', query: { error: 'There was an error adding the article. Please try again.' } });
        return null;
      });
    },
    addActivity() {
      this.$axios.post('/api/activities', this.newActivity).then((response) => {
        if (response.data === "") {
          this.$router.push({ path: '/activities', query: { error: 'Activity name already exists' } });
        } else {
          this.addedActivities.push(response.data);
          this.newActivity.name = '';
        }
      }).catch(error => {
        console.error("There was an error adding the activity:", error);
        this.$router.push({ path: '/activities', query: { error: 'There was an error adding the activity. Please try again.' } });
      });
    },
    addActivitiesToArticle(articleId) {
      const activityArticlePromises = this.addedActivities.map(activity => {
        const activityArticle = {
          articleId: articleId,
          activityId: activity.id
        };
        return this.$axios.post('/api/activities/addArticleActivity', activityArticle);
      });

      return Promise.all(activityArticlePromises).then(() => {
        this.$router.push('/articles');
      }).catch(error => {
        console.error("There was an error adding the activity articles:", error);
        this.$router.push({ path: '/articles', query: { error: 'There was an error adding the activity articles. Please try again.' } });
      });
    },
    submitArticle() {
      this.addArticle().then(article => {
        if (article) {
          this.addActivitiesToArticle(article.id);
        }
      });
    },
    fetchDestinations() {
      this.$axios.get('/api/destinations').then((response) => {
        this.destinations = response.data;
      }).catch(error => {
        console.error("There was an error fetching the destinations:", error);
      });
    },
    fetchDestinationById() {
      if (this.destinationId) {
        this.$axios.get(`/api/destinations/${this.destinationId}`).then((response) => {
          this.selectedDestination = response.data;
        }).catch(error => {
          console.error("There was an error fetching the destination:", error);
        });
      }
    },
    selectDestination(destination) {
      this.selectedDestination = destination;
    },
    selectActivity(activity) {
      this.selectedActivity = activity;
    }
  },
  created() {
    if (this.destinationId) {
      this.fetchDestinationById();
    } else {
      this.fetchDestinations();
    }
  }
};
</script>

<style scoped>
.form-group {
  margin-bottom: 1rem;
  max-width: 400px;
}
</style>
