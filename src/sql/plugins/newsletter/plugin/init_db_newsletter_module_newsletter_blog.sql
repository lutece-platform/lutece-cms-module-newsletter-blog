-- liquibase formatted sql
-- changeset newsletter-blog:init_db_newsletter_module_newsletter_blog.sql
-- preconditions onFail:MARK_RAN onError:WARN
DELETE FROM newsletter_template WHERE template_file_key= 'model_blogs.html';
INSERT INTO newsletter_template(description, template_file_key, picture_file_key, workgroup_key, topic_type) VALUES ('Modèle de liste de billets - Blog', 'model_blogs.html', 'model-blog.svg', 'all', 'NEWSLETTER_BLOG');

-- changeset newsletter-blog:init_db_newsletter_module_newsletter_blog.sql-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM newsletter_template WHERE template_file_key = 'model_blogs.html' AND id_template = 0
-- comment The template was inserted without identifier, so with the identifier 0 that the newsletter refuses: it gets the next free identifier
DELETE FROM newsletter_template WHERE template_file_key = 'model_blogs.html' AND id_template = 0;
INSERT INTO newsletter_template(id_template, description, template_file_key, picture_file_key, workgroup_key, topic_type) SELECT COALESCE(MAX(id_template), 0) + 1, 'Modèle de liste de billets - Blog', 'model_blogs.html', 'model-blog.svg', 'all', 'NEWSLETTER_BLOG' FROM newsletter_template;
