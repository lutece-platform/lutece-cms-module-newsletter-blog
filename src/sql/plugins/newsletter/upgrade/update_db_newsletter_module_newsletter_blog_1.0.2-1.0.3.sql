-- liquibase formatted sql
-- changeset newsletter-blog:update_db_newsletter_module_newsletter_blog_1.0.2-1.0.3.sql
-- preconditions onFail:MARK_RAN onError:WARN
DELETE FROM newsletter_template WHERE template_file_key= 'model_blogs.html';
INSERT INTO newsletter_template(description, template_file_key, picture_file_key, workgroup_key, topic_type) VALUES ('Modèle de liste de billets - Blog', 'model_blogs.html', 'model-blog.svg', 'all', 'NEWSLETTER_BLOG');
