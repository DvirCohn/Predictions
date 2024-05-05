package dto.implement;

public class GridDTO {
    private Integer numOfColumns;
    private Integer numOfRows;

    public GridDTO(Integer numOfColumns, Integer numOfRows) {
        this.numOfColumns = numOfColumns;
        this.numOfRows = numOfRows;
    }

    public Integer getNumOfColumns() {
        return numOfColumns;
    }

    public Integer getNumOfRows() {
        return numOfRows;
    }

    public StringBuffer getGridInfo(){
        StringBuffer info = new StringBuffer();
        info.append("Grid:"+"\n\n");
        info.append("# of Columns- "+this.getNumOfColumns()+"\n");
        info.append("# of Rows - "+this.getNumOfRows()+"\n");
        return info;
    }
}
