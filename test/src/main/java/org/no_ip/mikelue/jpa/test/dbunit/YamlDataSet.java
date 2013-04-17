package org.no_ip.mikelue.jpa.test.dbunit;

import org.apache.commons.lang3.mutable.MutableInt;
import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.CompositeTable;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.DefaultTableIterator;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.NoSuchColumnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is a {@link IDataSet} which accepts data from <a href="http://yaml.org/">YAML</a> format.<p>
 *
 * YAML syntax data set example:
 * <code><pre>
# table_name: row data
# rowdata: col_name; col_value, ....
tt_d1:
- { d1_id: 100, d1_account: "u001", d1_time_created: 2010-01-01 }
- { d1_id: 100, d1_account: "u002", d1_password: "password", d1_time_created: 2010-01-01 }
- { d1_account: "u003", d1_password: "03pp", d1_time_created: 2010-02-01 }

tt_d2:
    d2_id: 100
    d2_account: "u002"
 * </pre></code>
 *
 * The design principal of This class is based on {@link Yaml}.<p>
 *
 * This class uses <a href="http://www.snakeyaml.org/">SnakeYaml</a> to load YAML.<p>
 */
public class YamlDataSet extends AbstractDataSet {
    private Logger logger = LoggerFactory.getLogger(YamlDataSet.class);
    private ITable[] tablesOfData;

    /**
     * Initalize dataset of YAML with plain string data.<p>
     *
     * @param yaml the content of {@link String} in YAML format
     *
     * @see Yaml#load(String)
     */
    public YamlDataSet(String yaml)
    {
        logger.trace("Loading YAML data from String: {}", yaml);
        init(new Yaml().loadAs(yaml, Map.class));
    }
    /**
     * Initalize dataset of YAML from {@link InputStream}(BOM is respected and removed).<p>
     *
     * @param inputStream the content of string in YAML format
     *
     * @see Yaml#load(InputStream)
     */
    public YamlDataSet(InputStream inputStream)
    {
        logger.trace("Loading YAML data from InputStream: {}", inputStream);
        init(new Yaml().loadAs(inputStream, Map.class));
    }
    /**
     * Initalize dataset of YAML from {@link Reader}(BOM must not be present).<p>
     *
     * @param reader the content of string in YAML format
     *
     * @see Yaml#load(Reader)
     */
    public YamlDataSet(Reader reader)
    {
        logger.trace("Loading YAML data from Reader: {}", reader);
        init(new Yaml().loadAs(reader, Map.class));
    }

    @Override
    protected ITableIterator createIterator(boolean reversed)
    {
        return new DefaultTableIterator(tablesOfData, reversed);
    }

    @SuppressWarnings("unchecked")
    private void init(Map srcData)
    {
        Map<String, Object> yamlData = (Map<String, Object>)srcData;
        List<ITable> listOfTables = new ArrayList<ITable>(16);

        /**
         * Process every block and convert to ITable
         */
        for (Map.Entry<String, Object> tableEntry: yamlData.entrySet()) {
            String tableName = tableEntry.getKey();
            logger.debug("Processing table: {}", tableName);

            listOfTables.add(
                convertToITable(tableName, tableEntry.getValue())
            );
        }
        // :~)

        tablesOfData = listOfTables.toArray(new ITable[0]);
        logger.info("Number of tables: {}", tablesOfData.length);

        logDetailOfDataSet();
    }

    @SuppressWarnings("unchecked")
    private ITable convertToITable(String tableName, Object tableData)
    {
        /**
         * Multiple rows' data
         */
        if (List.class.isInstance(tableData)) {
            List<Map<String, Object>> typedTableData = (List<Map<String, Object>>)tableData;
            logger.debug(
                "Loading multiple rows' data. Table:[{}]. Count[{}]",
                tableName, typedTableData.size()
            );
            return makeITable(
                tableName, typedTableData
            );
        }
        // :~)

        /**
         * Single row's data
         */
        if (Map.class.isInstance(tableData)) {
            logger.debug("Loading single row's data. Table:[{}], Data:{}", tableName, tableData);
            return makeITable(
                tableName, Arrays.asList((Map<String, Object>)tableData)
            );
        }
        // :~)

        logger.error("The YAML format need to be \"Mapping\" or \"Sequence of Mappings\". Table: [{}].\nContent: {}",
            tableName, tableData
        );
        throw new RuntimeException(String.format(
            "Table[%s]'s data is not a YAML \"Mapping\" or \"Sequence of Mappings\". Not supported content: %s",
            tableName, tableData
        ));
    }

    private ITable makeITable(String tableName, List<Map<String, Object>> dataOfTable)
    {
        ITable resultTable = null;

        int r = 0;
        for (Map<String, Object> rowDefAndData: dataOfTable) {
            logger.trace("Processing table[{}], row number: {}", tableName, ++r);

            resultTable = mergeTwoTables(tableName, resultTable, makeITable(tableName, rowDefAndData));
            //if (resultTable == null) {
                //resultTable = makeITable(tableName, rowDefAndData);
            //} else {
                //resultTable = new CompositeTable(
                    //resultTable, makeITable(tableName, rowDefAndData)
                //);
            //}
        }

        return resultTable;
    }
    private ITable makeITable(String tableName, Map<String, Object> rowDefAndData)
    {
        /**
         * Build column definition
         */
        Column[] columns = new Column[rowDefAndData.size()];
        Object[] cellData = new Object[rowDefAndData.size()];

        int i = 0;
        for (Map.Entry<String, Object> cell: rowDefAndData.entrySet()) {
            logger.trace("Build Column: [{}], value: [{}]", cell.getKey(), cell.getValue());
            cellData[i] = cell.getValue();
            columns[i] = new Column(
                cell.getKey(),
                DataType.forObject(cellData[i])
            );

            i++;
        }
        // :~)

        DefaultTable resultTable = new DefaultTable(tableName, columns);

        try {
            resultTable.addRow(cellData);
        } catch (DataSetException e) {
            logger.error("Build row of data error", e);
            throw new RuntimeException(e);
        }

        return resultTable;
    }
    private ITable mergeTwoTables(String tableName, ITable leftTable, ITable rightTable)
    {
        if (leftTable == null) {
            return rightTable;
        }

        ITableMetaData leftTableMetaData = leftTable.getTableMetaData(),
                       rightTableMetaData = rightTable.getTableMetaData();

        /**
         * Compare columns of two tables
         */
        Set<Column> leftColumnSet, rightColumnSet;
        try {
            leftColumnSet = new HashSet<Column>(Arrays.asList(
                leftTableMetaData.getColumns()
            ));
            rightColumnSet = new HashSet<Column>(Arrays.asList(
                rightTableMetaData.getColumns()
            ));

        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }

        if (leftColumnSet.equals(rightColumnSet)) {
            logger.trace("[Combine] Two rows is equal.");
            return new CompositeTable(leftTable, rightTable);
        }
        // :~)

        /**
         * Generate a new meta data
         */
        Set<Column> finalColumnSet, combinedColumnSet;
        if (rightColumnSet.size() < leftColumnSet.size()) {
            logger.trace("[Combine] columns of rigth table to left table.");
            finalColumnSet = leftColumnSet;
            combinedColumnSet = rightColumnSet;
        } else {
            logger.trace("[Combine] columns of left table to right table.");
            finalColumnSet = rightColumnSet;
            combinedColumnSet = leftColumnSet;
        }
        for (Column combinedColumn: combinedColumnSet) {
            boolean added = finalColumnSet.add(combinedColumn);
            logger.trace(
                "[Combine] column[{}] result: {}",
                combinedColumn.getColumnName(), added
            );
        }

        logger.trace("[Combine] result: {}", finalColumnSet);
        // :~)

        DefaultTable newTable = new DefaultTable(
            new DefaultTableMetaData(
                tableName,
                finalColumnSet.toArray(new Column[0])
            )
        );

        /**
         * Merge data into new table
         */
        MutableInt rowNumber = new MutableInt(0);

        mergeDataToTable(newTable, leftTable, rowNumber);
        mergeDataToTable(newTable, rightTable, rowNumber);
        // :~)

        return newTable;
    }

    private void mergeDataToTable(DefaultTable mergeTable, ITable srcTable, MutableInt numberOfRow)
    {
        logger.debug("Merge table rows: [{}]. Source table rows: [{}]",
            mergeTable.getRowCount(), srcTable.getRowCount()
        );

        for (int i = 0; i < srcTable.getRowCount(); i++) {
            try {
                mergeTable.addRow();

                for (Column c: srcTable.getTableMetaData().getColumns()) {
                    Object value = srcTable.getValue(i, c.getColumnName());

                    logger.debug(
                        "Merger data. Row: [{}]. Column: [{}]. Data: [{}]",
                        new Object[] {
                            i, c.getColumnName(), value
                        }
                    );

                    mergeTable.setValue(
                        numberOfRow.intValue(), c.getColumnName(),
                        value
                    );
                }
            } catch (DataSetException e) {
                logger.error("Merge data into new table error", e);
                throw new RuntimeException(e);
            }

            numberOfRow.increment();
        }
    }

    private void logDetailOfDataSet()
    {
        if (!logger.isDebugEnabled()) {
            return;
        }

        /**
         * Logger content of dataset
         */
        NEXT_TABLE: for (ITable table: tablesOfData) {
            logger.debug("Result table: [{}], row count: [{}], Meta data: [{}]",
                new Object[] {
                    table.getTableMetaData().getTableName(),
                    table.getRowCount(),
                    table.getTableMetaData()
                }
            );

            if (!logger.isTraceEnabled()) {
                continue NEXT_TABLE;
            }

            /**
             * Log content of table
             */
            Column[] resultColumns;
            try {
                resultColumns = table.getTableMetaData().getColumns();
            } catch (DataSetException e) {
                throw new RuntimeException(e);
            }

            for (int r = 0; r < table.getRowCount(); r++) {
                for (Column c: resultColumns) {
                    Object columnValue = null;
                    try {
                        columnValue = table.getValue(r, c.getColumnName());
                    } catch (NoSuchColumnException e) {
                    } catch (DataSetException e) {
                        throw new RuntimeException(e);
                    }

                    logger.trace(
                        "\tRow: [#{}], [{}] -> [{}]",
                        new Object[] {
                            r, c.getColumnName(),
                            columnValue == null ? "<NULL>" : columnValue
                        }
                    );
                }
            }
            // :~)
        }
        // :~)
    }
}
