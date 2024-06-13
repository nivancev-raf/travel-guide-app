<template>
    <div class="edit-article">
      <h2>Edit Article</h2>
      <form @submit.prevent="editArticle">
        <div class="form-group">
          <label for="title">Title</label>
          <input type="text" v-model="newArticle.title" class="form-control" id="title" required>
        </div>
        <div class="form-group">
          <b-dropdown id="dropdown-1" :text="destinationName.name" class="m-md-2 btn-primary" toggle-class="btn-primary">
            <!-- <b-dropdown-item v-for="destination in destinations" :key="destination.id" @click="selectDestination(destination)">
              {{ destination.name }}
            </b-dropdown-item> -->
          </b-dropdown>
        </div>
        <div class="form-group">
          <label for="text">Text</label>
          <textarea v-model="newArticle.text" class="form-control" id="text" required></textarea>
        </div>
        <div class="form-group">
          <b-dropdown id="dropdown-2" :text="dropdownText" class="m-md-2 btn-primary" toggle-class="btn-primary">
            <b-dropdown-item v-for="activity in activities" :key="activity.id" @click="selectActivity(activity)">
              {{ activity.name }}
            </b-dropdown-item>
          </b-dropdown>
        </div>
        <div>
          <h4 v-if="selectedActivity">{{ selectedActivityText }} {{selectedActivity.name}}</h4>
          <h4 v-else> {{ dropdownText }}</h4>
          <button type="button" class="btn btn-danger" v-if="selectedActivity" @click="deleteActivity">Delete selected activity</button>
        </div>
        <br>
        <div class="form-group">
          <label for="newActivity">New Activity</label>
          <input type="text" v-model="newActivity.name" class="form-control" id="newActivity">
        </div>
        <div class="form-group">
          <button type="button" class="btn btn-primary" @click="addActivity">Add New Activity</button>
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-success">Edit Article</button>
        </div>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        newArticle: {
          title: '',
          text: '',
          destinationId: null,
          authorId: null, // Initialize the newArticle object with default values
          creationTime: null,
          visitCount: 0
        },
        newActivity: {
          name: ''
        },
        destinationName: '',
        activities: [],
        selectedActivity: null,
        selectedActivityText: ''
      };
    },
    computed: {
      dropdownText() {
        if (this.activities && this.activities.length > 0) {
            // set selectedActivityText to emty string if selectedActivity is null
            
          return this.selectedActivity ? this.selectedActivity.name : 'Select Activity';
        } else {
          return 'No activities';
        }
      }
    },
    methods: {
      fetchArticleById() {
        // this.$route.params.id is the article ID
        this.$axios.get(`/api/articles/${this.$route.params.id}`).then((response) => {
          this.newArticle = response.data;
          this.fetchDestinationById(this.newArticle.destinationId);
        }).catch(error => {
          console.error("There was an error fetching the article:", error);
        });
      },
      fetchDestinationById(id) {
        this.$axios.get(`/api/destinations/${id}`).then((response) => {
          this.destinationName = response.data;
        }).catch(error => {
          console.error("There was an error fetching the destination:", error);
        });
      },
      fetchActivitiesByArticleId(articleId) {
        this.$axios.get(`/api/activities/article/${articleId}`).then((response) => {
          this.activities = response.data;
        }).catch(error => {
          console.error("There was an error fetching the activities:", error);
        });
      },
      selectActivity(activity) {
        this.selectedActivity = activity;
      },
      addActivity() {
        this.$axios.post('/api/activities', this.newActivity).then((response) => {
          if (response.data === "") {
            this.$router.push({ path: '/activities', query: { error: 'Activity name already exists' } });
          } else {
            this.activities.push(response.data);
            this.newActivity.name = '';
            this.selectedActivity = response.data;
  
            // Add the activity to the article
            const articleActivity = {
              articleId: this.newArticle.id,
              activityId: response.data.id
            };
            this.$axios.post('/api/activities/addArticleActivity', articleActivity).then(() => {
              console.log("Activity added to article - ARTICLEACTIVITY TABLE UPDATED");
              //   this.$router.push('/articles');
            }).catch(error => {
              console.error("There was an error adding the activity to the article:", error);
              this.$router.push({ path: '/articles', query: { error: 'There was an error adding the activity to the article. Please try again.' } });
            });
          }
        }).catch(error => {
          console.error("There was an error adding the activity:", error);
          this.$router.push({ path: '/activities', query: { error: 'There was an error adding the activity. Please try again.' } });
        });
      },
      editArticle() {
        this.$axios.put(`/api/articles/${this.newArticle.id}`, this.newArticle).then(() => {
          this.$router.push('/articles');
        }).catch(error => {
          console.error("There was an error editing the article:", error);
          this.$router.push({ path: '/articles', query: { error: 'There was an error editing the article. Please try again.' } });
        });
      },
      deleteActivity() {
        if (confirm('Are you sure you want to delete this activity?')) {
          this.$axios.delete(`/api/activities/${this.selectedActivity.id}`).then(() => {
            this.activities = this.activities.filter(activity => activity.id !== this.selectedActivity.id);
            this.selectedActivity = null;
          }).catch(error => {
            console.error("There was an error deleting the activity:", error);
          });
        }
      }
    },
    created() {
      this.fetchArticleById();
      this.fetchActivitiesByArticleId(this.$route.params.id);
      this.selectedActivityText = "Selected Activity: ";
    }
  };
  </script>
  
  <style scoped>
  .form-group {
    margin-bottom: 1rem;
    max-width: 400px;
  }
  </style>
  