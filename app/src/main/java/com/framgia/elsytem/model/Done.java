package com.framgia.elsytem.model;

import com.framgia.elsytem.utils.Constants;

/**
 * Created by ahsan on 1/4/16.
 */
public class Done {
    int category_id;
    int state = Constants.ZERO;

    public Done(int category_id, int state) {

        this.category_id = category_id;
        this.state = state;
    }

    public int statereturn(int category_id) {
        return state;
    }

    public int category_return() {
        return category_id;
    }

}
