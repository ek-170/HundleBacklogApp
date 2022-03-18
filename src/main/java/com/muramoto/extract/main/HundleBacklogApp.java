package com.muramoto.extract.main;

import com.muramoto.extract.backlog.HundleIssue;

public class HundleBacklogApp{
    public static void main( String[] args ){ 
      HundleIssue hi = new HundleIssue();
      hi.extractIssues();
    }
}