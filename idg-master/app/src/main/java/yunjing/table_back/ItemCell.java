package yunjing.table_back;

public class ItemCell {
	private String cellValue = "";//单元格的值
	private int cellSpan = 1; //单元格跨列
	private CellTypeEnum cellType = CellTypeEnum.LABEL; //单元格类型
	private int colNum = 0;  //单元格列号，从0开始
	private int rowNum = 0;//从0开始，每个item都从0开始
	private int rowSpan = 1;//单元格跨行
	//private int rowType = 0; //行类型

	private boolean isChange = false;//是否被编辑
	public ItemCell(String cellValue, CellTypeEnum cellType, int cellSpan){
		this.cellValue = cellValue;
		this.cellType = cellType;
		this.cellSpan = cellSpan;
	}
	public ItemCell(String cellValue, CellTypeEnum cellType){
		this(cellValue,cellType,1);
	}
	public int getColNum(){
		return this.colNum;
	}
	//	public void setRowType(int rowType){
//		this.rowType = rowType;
//	}
//	public int getRowType(){
//		return this.rowType;
//	}
	public String getCellValue(){
		return cellValue;
	}
	public void setCellValue(String value){
		this.cellValue = value;
	}
	public CellTypeEnum getCellType(){
		return cellType;
	}
	public int getCellSpan(){
		return cellSpan;
	}
	public void setIsChange(boolean isChange){
		this.isChange = isChange;
	}
	public boolean getIsChange(){
		return this.isChange;
	}
	//设置行列位置,列根据前面列+rowspan数字累加后的值，rownum每行都从0开始
	public void setPos(int rowNum,int colNum,int rowSpan){
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.rowSpan = rowSpan;
	}
	public int getRowNum() {
		return rowNum;
	}
	public int getRowSpan() {
		return rowSpan;
	}
	public int getId(){
		return this.rowNum * 10000 + this.rowSpan;
	}
}
