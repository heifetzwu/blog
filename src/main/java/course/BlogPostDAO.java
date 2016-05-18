package course;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public Document findByPermalink(String permalink) {

        // todo  XXX
        Document post = null;

        FindIterable <Document> iterable = postsCollection.find(new Document("permalink",permalink));
        post = iterable.first();

        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<Document> findByDateDescending(int limit) {

        // todo,  XXX
        // Return a list of Documents, each one a post from the posts collection
        // List<Document> posts = null;
        List<Document> posts = new  ArrayList <Document> ();
        
        
        FindIterable <Document> iterable = postsCollection.find().sort(new Document("date", -1)).limit(limit);
        
        for (Document doc : iterable) {
        	posts.add(doc);
System.out.print("######################" + doc);        	
        }
       
                
        //posts = postsCollection.find

        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();
        permalink = permalink+ (new Date()).getTime();


        // todo XXX
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        // Build the post object and insert it
        Document post = new Document();
        post.append("title", title).append("author", username).append("body", body);
        post.append("tags", tags);
        post.append("date", new Date());
        post.append("permalink", permalink);
        postsCollection.insertOne(post);
        


        return permalink;
    }




    // White space to protect the innocent








    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {
    	
    	// Document post = findByPermalink ( permalink);
    	
    	Document comment = new Document("author", name).append("body", body);
    	
    	if (email == null || email.equals("")) {
    		
    	} else {
    		comment.append("email", email);
    	}
        
    	
    	
    	postsCollection.updateOne(new Document("permalink", permalink), new Document("$push", new Document("comments",comment )));
        // todo  XXX
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments

    }
}
