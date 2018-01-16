--
-- Table structure for table newsletter_blogs_topic
--
DROP TABLE IF EXISTS newsletter_blogs_topic;
CREATE TABLE newsletter_blogs_topic (
  id_topic INT NOT NULL,
  id_template INT NOT NULL,
  use_tags SMALLINT NOT NULL,
  PRIMARY KEY (id_topic)
);

--
-- Table structure for table newsletter_blogs_tag
--
DROP TABLE IF EXISTS newsletter_blogs_tag;
CREATE TABLE newsletter_blogs_tag (
  id_tag INT NOT NULL,
  id_topic INT NOT NULL,
  PRIMARY KEY (id_tag,id_topic)
);

--
-- Table structure for table newsletter_blogs_portlet
--
DROP TABLE IF EXISTS newsletter_blogs_portlet;
CREATE TABLE newsletter_blogs_portlet (
  id_portlet INT NOT NULL,
  id_topic INT NOT NULL,
  PRIMARY KEY (id_portlet,id_topic)
);