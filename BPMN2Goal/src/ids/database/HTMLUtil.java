package ids.database;

public class HTMLUtil {

	static String getPage() {
		return getMetaData();
	}

	static String getMetaData() {
		return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n";
	}

	static String getTitle(String title) {
		return "<TITLE>" + title + "</TITLE>\n";
	}

	static String getHead(String head_content) {
		return "<HEAD>" + head_content + "</HEAD>\n";
	}

}
