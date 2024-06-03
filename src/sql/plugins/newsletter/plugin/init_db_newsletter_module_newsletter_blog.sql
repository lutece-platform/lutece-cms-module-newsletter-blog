DELETE FROM newsletter_template WHERE template_file_key= 'model_blogs.html';
INSERT INTO newsletter_template(description, template_file_key, picture_file_key, workgroup_key, topic_type) VALUES ('Modèle de liste de billets - Blog', 'model_blogs.html', 'model-blog.svg', 'all', 'NEWSLETTER_BLOG');
