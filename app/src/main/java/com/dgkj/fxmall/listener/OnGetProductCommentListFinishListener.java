package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.CommentBean;

import java.util.List;

/**
 * Created by Android004 on 2017/4/25.
 */

public interface OnGetProductCommentListFinishListener {
    void onGetProductCommentListFinished(List<CommentBean> comments);
}
