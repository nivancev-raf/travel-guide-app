
# Travel Guide Platform

**Travel Guide Platform** is a full-stack web application developed as part of the "Web Programming" course. The application provides a comprehensive online platform where users can explore and discover various travel destinations. It features both a content management system (CMS) for managing travel articles and a public-facing platform for reading and interacting with content.

## Project Overview

The Travel Guide Platform is designed to allow content creators to manage travel articles, categorize them by destination, and provide a rich user experience for exploring these articles. The platform is built using Vue.js for the frontend and JAX-RS for the backend, ensuring a robust and scalable architecture.

### Key Features:
- **Content Management System (CMS)**: 
  - User authentication and authorization with roles (admin and content editor).
  - Manage destinations and travel articles.
  - Admin capabilities to manage users and their roles.

- **Public Platform**:
  - Browse and search for travel articles by destination or activity.
  - View most popular and recent articles.
  - Comment on articles and view comments from other users.

### Technologies Used

- **Frontend**: Vue.js
- **Backend**: JAX-RS
- **Styling**: CSS
- **Database**: MySQL

## Getting Started

### Prerequisites
- Node.js and npm (for Vue.js)
- Java 8 or higher (for JAX-RS)
- A relational database (if applicable)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/nivancev-raf/travel-guide-app.git
   ```

2. **Frontend Setup**:
   - Navigate to the frontend directory:
     ```bash
     cd frontend
     ```
   - Install dependencies and run the development server:
     ```bash
     npm install
     npm run serve
     ```

3. **Backend Setup**:
   - Navigate to the backend directory:
     ```bash
     cd backend
     ```
   - Build and deploy the backend:
     ```bash
     mvn clean install
     mvn jetty:run
     ```

4. **Database Setup**:
   - Set up the database schema if applicable.

### Usage

- **CMS**: Login with an admin or content editor account to manage destinations and articles.
- **Public Platform**: Explore travel articles, read detailed content, and engage with other users through comments.

## Contact
For any questions or feedback, please reach out to nivancev02@gmail.com.

