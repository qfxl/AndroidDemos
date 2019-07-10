package com.qfxl.poi

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //注意权限问题
        btn_parse_excel.setOnClickListener {
            "${Environment.getExternalStorageDirectory()}/POI-Test.xlsx".also { path ->
                File(path).also { f ->
                    if (f.exists()) {
                        try {
                            //获取工作簿
                            XSSFWorkbook(FileInputStream(f)).apply {
                                val evaluator = creationHelper.createFormulaEvaluator()
                                //获取第一个sheet
                                getSheetAt(0).apply {
                                    //当前sheet的所有内容
                                    val content = StringBuilder()
                                    //获取行数
                                    val rowCount = physicalNumberOfRows
                                    //获取每一行的列数
                                    var columnCount: Int
                                    for (r in 0 until rowCount) {
                                        content.append("\n第$r 行数据  ")
                                        columnCount = getRow(r).physicalNumberOfCells
                                        val currentRow = getRow(r)
                                        for (c in 0 until columnCount) {
                                            content.append(getCellString(currentRow, c, evaluator))
                                                .append("  ")
                                        }
                                    }
                                    "数据读取完毕\n $content".log()
                                }
                            }
                        } catch (e: Exception) {
                            e.message?.log()
                        }
                    }
                }
            }
        }

        btn_write_excel.setOnClickListener {
            val dataList = mutableListOf(
                ExcelVo("张三", "A101", 85.5),
                ExcelVo("李四", "B102", 60.0),
                ExcelVo("王五", "C103", 78.0),
                ExcelVo("周六", "D104", 98.0)
            )
            val outputFile = File("${Environment.getExternalStorageDirectory()}/POI-Test-Write.xlsx")
            XSSFWorkbook().also { book ->
                try {
                    //创建sheet
                    book.createSheet("sheet0").apply {
                        //创建行
                        createRow(0).apply {
                            //创建列
                            createCell(0).setCellValue("姓名")
                            createCell(1).setCellValue("学号")
                            createCell(2).setCellValue("成绩")
                        }
                        for (c in 0 until dataList.size) {
                            createRow(c + 1).apply {
                                val vo = dataList[c]
                                createCell(0).setCellValue(vo.name)
                                createCell(1).setCellValue(vo.sn)
                                createCell(2).setCellValue(vo.results)
                            }
                        }
                    }
                    book.write(FileOutputStream(outputFile))
                } catch (e: Exception) {
                    e.message?.log()
                }
            }
        }
        btn_cell_style.setOnClickListener {
            val outputFile = File("${Environment.getExternalStorageDirectory()}/POI-1.xlsx")
            XSSFWorkbook().also { book ->
                try {
                    //创建sheet
                    book.createSheet("sheet0").apply {
                        //创建行
                        createRow(0).apply {
                            height = 1500
                            //第二列列宽
                            setColumnWidth(1, 5000)
                            //创建列
                            createCell(0)
                                .setCellValue("测试高度")
                            createCell(1).apply {
                                cellStyle = book.createCellStyle().also { style ->
                                    style.alignment = XSSFCellStyle.ALIGN_CENTER
                                    style.verticalAlignment = XSSFCellStyle.VERTICAL_CENTER
                                }
                            }.setCellValue("测试对其方式")
                            createCell(2).apply {
                                cellStyle = book.createCellStyle().also { style ->
                                    style.borderBottom = XSSFCellStyle.BORDER_DOUBLE
                                    style.borderLeft = XSSFCellStyle.BORDER_THICK
                                    style.borderTop = XSSFCellStyle.BIG_SPOTS
                                    style.topBorderColor = IndexedColors.BLUE.index
                                }
                            }.setCellValue("测试外边框")
                            createCell(3).apply {
                                cellStyle = book.createCellStyle().also { style ->
                                    style.fillBackgroundColor = HSSFColor.RED.index
                                    style.fillPattern = XSSFCellStyle.LESS_DOTS
                                }
                            }.setCellValue("测试填充色")
                            createCell(4).apply {
                                cellStyle = book.createCellStyle().also { style ->
                                    style.rotation = 90
                                }
                            }.setCellValue("测试旋转")
                        }

                    }
                    book.write(FileOutputStream(outputFile))
                } catch (e: Exception) {
                    e.message?.log()
                }
            }
        }
    }

    private fun getCellString(r: Row, position: Int, formulaEvaluator: FormulaEvaluator): String {
        val cell = r.getCell(position)
        val cellValue = formulaEvaluator.evaluate(cell)
        return when (cellValue.cellType) {
            Cell.CELL_TYPE_NUMERIC -> {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    val date = cellValue.numberValue
                    val formatter = SimpleDateFormat("MM/dd/yy", Locale.CHINA)
                    formatter.format(HSSFDateUtil.getJavaDate(date))
                } else {
                    cellValue.numberValue.toString()
                }
            }
            Cell.CELL_TYPE_STRING -> {
                return cellValue.stringValue
            }
            Cell.CELL_TYPE_BOOLEAN -> {
                return cellValue.booleanValue.toString()
            }
            Cell.CELL_TYPE_BLANK -> {
                return "空"
            }
            else -> {
                cellValue.toString()
            }
        }
    }

    fun String.log() {
        Log.i("qfxl", this)
    }

    data class ExcelVo(val name: String, val sn: String, val results: Double)
}
