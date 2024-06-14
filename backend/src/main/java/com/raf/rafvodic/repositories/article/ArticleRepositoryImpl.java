package com.raf.rafvodic.repositories.article;

import com.raf.rafvodic.entities.Activity;
import com.raf.rafvodic.entities.Article;
import com.raf.rafvodic.entities.Destination;
import com.raf.rafvodic.entities.User;
import com.raf.rafvodic.enums.UserStatus;
import com.raf.rafvodic.enums.UserType;
import com.raf.rafvodic.repositories.MySqlAbstractRepository;
import com.raf.rafvodic.repositories.destination.DestinationRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepositoryImpl extends MySqlAbstractRepository implements ArticleRepository {
    @Override
    public Article addArticle(Article article) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"}; // id is autoincrementq

            preparedStatement = connection.prepareStatement("INSERT INTO articles (title, text, creation_time, visit_count, author_id, destination_id) VALUES(?, ?, ?, ?, ?, ?)", generatedColumns);
            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setString(2, article.getText());
            preparedStatement.setTimestamp(3, article.getCreationTime());
            preparedStatement.setInt(4, article.getVisitCount());
            preparedStatement.setInt(5, article.getAuthorId());
            preparedStatement.setInt(6, article.getDestinationId());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                article.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return article;
    }

    @Override
    public Article findArticleById(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Article article = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM articles WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                article = new Article();
                article.setId(resultSet.getInt("id"));
                article.setTitle(resultSet.getString("title"));
                article.setText(resultSet.getString("text"));
                article.setCreationTime(resultSet.getTimestamp("creation_time"));
                article.setVisitCount(resultSet.getInt("visit_count"));
                article.setAuthorId(resultSet.getInt("author_id"));
                article.setDestinationId(resultSet.getInt("destination_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return article;
    }

    @Override
    public Article findArticleByName(String name) {
        return null;
    }


    @Override
    public List<Article> getAllArticles(int offset, int limit) {
        List<Article> articlesList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            String query = "SELECT * FROM articles LIMIT ? OFFSET ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                String text = resultSet.getString("text");
                Timestamp creationTime = resultSet.getTimestamp("creation_time");
                int visitCount = resultSet.getInt("visit_count");
                int destinationId = resultSet.getInt("destination_id");

                articlesList.add(new Article(id, title, text, creationTime, visitCount, authorId, destinationId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return articlesList;
    }


    @Override
    public List<Article> getArticlesByActivityId(int activityId) {
        List<Article> articleList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            // Join the articleactivities and articles table to get the articles related to the activity
            String query = "SELECT a.id, a.destination_id, a.author_id, a.visit_count, a.creation_time, a.text, a.title " +
                    "FROM articleactivities aa " +
                    "JOIN articles a ON aa.article_id = a.id " +
                    "WHERE aa.activity_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, activityId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int articleId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                String text = resultSet.getString("text");
                Timestamp creationTime = resultSet.getTimestamp("creation_time");
                int visitCount = resultSet.getInt("visit_count");
                int destinationId = resultSet.getInt("destination_id");
                articleList.add(new Article(articleId, title, text, creationTime, visitCount, authorId, destinationId));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return articleList;


    }

    @Override
    public List<Article> findMostReadArticles(int limit, String sort, String filter) {
        List<Article> articles = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            // creationTime,gt,2024-05-03T15:54:05.262Z"
            String[] filterParts = filter.split(",");
            String filterColumn = filterParts[0];
            String filterOperator = filterParts[1];
            String filterValue = filterParts[2];

            // Translate the filter operator to SQL
            String sqlOperator;
            switch (filterOperator) {
                case "gt":
                    sqlOperator = ">";
                    break;
                case "lt":
                    sqlOperator = "<";
                    break;
                case "eq":
                    sqlOperator = "=";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid filter operator: " + filterOperator);
            }


            String query = "SELECT * FROM articles WHERE " + filterColumn + " " + sqlOperator + " ? ORDER BY " + sort.replace(",", " ") + " LIMIT ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, filterValue);
            preparedStatement.setInt(2, limit);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String text = resultSet.getString("text");
                Timestamp creationTime = resultSet.getTimestamp("creation_time");
                int visitCount = resultSet.getInt("visit_count");
                int authorId = resultSet.getInt("author_id");
                int destinationId = resultSet.getInt("destination_id");

                articles.add(new Article(id, title, text, creationTime, visitCount, authorId, destinationId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public Article updateArticle(Article article) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("UPDATE articles SET title = ?, text = ?, creation_time = ?, visit_count = ?, author_id = ?, destination_id = ? WHERE id = ?");
            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setString(2, article.getText());
            preparedStatement.setTimestamp(3, article.getCreationTime());
            preparedStatement.setInt(4, article.getVisitCount());
            preparedStatement.setInt(5, article.getAuthorId());
            preparedStatement.setInt(6, article.getDestinationId());
            preparedStatement.setInt(7, article.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
    }
        finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return article;
    }

    @Override
    public void deleteArticle(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM articles WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public List<Article> getArticlesByDestinationId(int destinationId) {
        List<Article> articlesList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM articles where destination_id = ?");
            preparedStatement.setInt(1, destinationId);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                String text = resultSet.getString("text");
                Timestamp creationTime = resultSet.getTimestamp("creation_time");
                int visitCount = resultSet.getInt("visit_count");
                int destination_Id = resultSet.getInt("destination_id");

                articlesList.add(new Article(id, title, text, creationTime, visitCount, authorId, destination_Id));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return articlesList;
    }

}
