package com.hackbulgaria.antoan.expenselist;

/**
 * Created by Antoan on 03-Dec-14.
 */
public class FluentApiBuilder {

    public static final String DELETE = "delete";
    public static final String UPDATE = "update";
    public static final String EQUAL_SIGN = "=";
    public static final String GREATER_THAN_SIGN = ">";
    public static final String LESS_THAN_SIGN = "<";
    public static final String SELECT = "select";
    private String mTableName;
    private String mWhere;
    private String mCompareWhat;
    private final static String QUERY_SELECT_TEMPLATE = "SELECT %s FROM %s";
    private final static String QUERY_UPDATE_TEMPLATE = "UPDATE %s SET %s";
    private final static String QUERY_DELETE_TEMPLATE = "DELETE FROM %s";
    private String mComparison;
    private String mFunction;
    private String mFunctionWhat;
    private String mSet;

    public FluentApiBuilder() {

    }

    private void reset() {
        mTableName = null;
        mWhere = null;
        mCompareWhat = null;
    }

    public FluentApiBuilder select(String what) {
        this.mFunction = SELECT;
        this.mFunctionWhat = what;
        return this;
    }

    public FluentApiBuilder set(String set) {
        this.mSet = set;
        return this;
    }

    public FluentApiBuilder delete() {
        this.mFunction = DELETE;
        return this;
    }

    public FluentApiBuilder update(String what) {
        this.mFunction = UPDATE;
        this.mFunctionWhat = what;
        return this;
    }

    public FluentApiBuilder from(String tableName) {
        this.mTableName = tableName;
        return this;
    }

    public FluentApiBuilder where(String where) {
        this.mWhere = where;
        return this;
    }

    public FluentApiBuilder eq(String what) {
        this.mCompareWhat = what;
        this.mComparison = EQUAL_SIGN;
        return this;
    }

    public FluentApiBuilder gt(String what) {
        this.mCompareWhat = what;
        this.mComparison = GREATER_THAN_SIGN;
        return this;
    }

    public FluentApiBuilder ls(String what) {
        this.mCompareWhat = what;
        this.mComparison = LESS_THAN_SIGN;
        return this;
    }

    public String build() {
        StringBuilder res = new StringBuilder();

        if(mFunction.equals(SELECT)) {
            res.append(String.format(QUERY_SELECT_TEMPLATE, mFunctionWhat, mTableName));
        }
        else if(mFunction.equals(UPDATE)) {
            res.append(String.format(QUERY_UPDATE_TEMPLATE, mFunctionWhat, mSet));
        }
        else if(mFunction.equals(DELETE)) {
            res.append(String.format(QUERY_DELETE_TEMPLATE, mTableName));
        }

        if(mWhere != null) {
            res.append(" WHERE " + mWhere + " " + mComparison + " " + mCompareWhat);
        }

        reset();

        return res.toString();
    }
}

