import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

class SudoFileFilter extends FileFilter {
	String description;

	String extensions[];

	public SudoFileFilter(String description, String extension) {
		this(description, new String[] { extension });
	}

	public SudoFileFilter(String description, String extensions[]) {
		if (description == null)
			this.description = extensions[0] + "{ " + extensions.length + "} ";
		else
			this.description = description;
		this.extensions = (String[]) extensions.clone();
		toLower(this.extensions);
	}

	private void toLower(String array[]) {
		for (int i = 0, n = array.length; i < n; i++)
			array[i] = array[i].toLowerCase();
	}

	public String getDescription() {
		return description;
	}

	public boolean accept(File file) {
		if (file.isDirectory())
			return false;
//		else{
//			String path = file.getAbsolutePath().toLowerCase();
//			for (int i = 0, n = extensions.length; i < n; i++) {
//				String extension = extensions[i];
//				if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.'))
//					return true;
//			}
//		}
		return false;
	}
}

public class JFileChooserWithCustomizedFilter {

  public static void main(String[] a) {
    JFileChooser fileChooser = new JFileChooser("./Saves");
    FileFilter txtFilter = new SudoFileFilter(null, new String[] { "TXT" });

    fileChooser.addChoosableFileFilter(txtFilter);
    
    fileChooser.showOpenDialog(null);
  }

}