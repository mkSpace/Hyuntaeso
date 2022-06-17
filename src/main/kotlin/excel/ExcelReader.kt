package excel

import data.Data
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream

object ExcelReader {

    fun xlsToDataList(filePath: String): List<Data> {
        val dataList = mutableListOf<Data>()
        val inputStream = FileInputStream(filePath);
        val workBook = XSSFWorkbook(inputStream)

        repeat(workBook.numberOfSheets) { sheetIndex ->
            val currentSheet = workBook.getSheetAt(sheetIndex)
            repeat(currentSheet.physicalNumberOfRows) { rowIndex ->
                if (rowIndex != 0) {
                    val currentRow = currentSheet.getRow(rowIndex)
                    if (currentRow.getCell(0)?.stringCellValue?.isNotBlank() == true) {
                        val newData = Data().apply {
                            repeat(currentRow.physicalNumberOfCells) { cellIndex ->
                                val currentCell = currentRow.getCell(cellIndex)
                                val value: String = when (currentCell.cellType) {
                                    CellType.FORMULA -> currentCell.cellFormula
                                    CellType.NUMERIC -> currentCell.numericCellValue.toString()
                                    CellType.STRING -> currentCell.stringCellValue
                                    CellType.BLANK -> ""
                                    CellType.ERROR -> currentCell.errorCellString
                                    else -> ""
                                }
                                when (cellIndex) {
                                    0 -> this.type = if (value == "spam") Data.Type.SPAM else Data.Type.HAM
                                    1 -> this.content = value
                                }
                            }
                        }
                        dataList.add(newData)
                    }
                }
            }
        }
        return dataList
    }
}