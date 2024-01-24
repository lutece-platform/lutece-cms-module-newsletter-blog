DELETE FROM newsletter_template WHERE file_name= 'model_blogs.html';
INSERT INTO newsletter_template(description, file_name, picture, workgroup_key, topic_type) VALUES ('Modèle de liste de billets - Blog', 'model_blogs.html', 'model-blog.svg', 'all', 'NEWSLETTER_BLOG');
