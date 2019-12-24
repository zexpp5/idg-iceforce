package newProject.company.invested_project.bean;

import java.util.List;
import org.jaaksi.pickerview.dataset.OptionDataSet;

/**
 * description：
 */

public class County implements OptionDataSet {
  public int id;
  public String name;

  @Override
  public CharSequence getCharSequence() {
    return name;
  }

  @Override
  public List<OptionDataSet> getSubs() {
    return null;
  }

  @Override
  public String getValue() {
    return String.valueOf(id);
  }
}
