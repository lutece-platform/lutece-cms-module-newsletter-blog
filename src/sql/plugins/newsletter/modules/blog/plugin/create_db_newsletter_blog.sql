--
-- Table structure for table newsletter_htmldocs_topic
--
DROP TABLE IF EXISTS newsletter_htmldocs_topic;
CREATE TABLE newsletter_htmldocs_topic (
  id_topic INT NOT NULL,
  id_template INT NOT NULL,
  use_tags SMALLINT NOT NULL,
  PRIMARY KEY (id_topic)
);

--
-- Table structure for table newsletter_htmldocs_tag
--
DROP TABLE IF EXISTS newsletter_htmldocs_tag;
CREATE TABLE newsletter_htmldocs_tag (
  id_tag INT NOT NULL,
  id_topic INT NOT NULL,
  PRIMARY KEY (id_tag,id_topic)
);

--
-- Table structure for table newsletter_htmldocs_portlet
--
DROP TABLE IF EXISTS newsletter_htmldocs_portlet;
CREATE TABLE newsletter_htmldocs_portlet (
  id_portlet INT NOT NULL,
  id_topic INT NOT NULL,
  PRIMARY KEY (id_portlet,id_topic)
);