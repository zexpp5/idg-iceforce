package newProject.company.invested_project.bean;

import java.util.List;
import org.jaaksi.pickerview.dataset.OptionDataSet;

/**
 * description：
 */

public class Province implements OptionDataSet {
  public int id;
  public String name;
  public List<City> citys;

  @Override
  public CharSequence getCharSequence() {
    return name;
  }

  @Override
  public List<City> getSubs() {
    return citys;
  }

  @Override
  public String getValue() {
    return String.valueOf(id);
  }
}
