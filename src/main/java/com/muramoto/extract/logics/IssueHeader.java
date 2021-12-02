package com.muramoto.extract.logics;

import java.util.List;
import java.util.ArrayList;

public enum IssueHeader implements Header{
    NewUpdate("New/Update"), 
    Key("キー"), 
    Summary("件名"),
    Description("詳細"),
    Comment("コメント"),
    IssueType("種別"),
    Status("状態"),
    MileStone("マイルストーン"),
    Version("発生バージョン"),
    ExpectedTime("予定時間"),
    ActualTime("実績時間"),
    Assignee("担当者"),
    CreatedUser("作成者"),
    Created("作成日");

    private String label;

    private IssueHeader(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public List<String> getLabelList(){
        List<String> result = new ArrayList<>();
        for(IssueHeader ih : IssueHeader.values()){
            result.add(ih.getLabel());
        }

        return result;
    }
}