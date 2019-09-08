; (function ()
{
    // window对象上使用的名字
    let exportName = 'MyDatePick';
    // datebox类名
    let dateboxCls = 'date-box';
    // 触发日期框的INPUT的JQ对象引用
    let inputJQ = null;
    // 日期框JQ对象
    let dateboxJQ = null;
    // 日期框运行时数据
    let cfg = null;

    // 在input上使用此方法. <input onclick="MyDatePick()" />,需要时间部分: MyDatePick({fmt:datetime})
    let mydate = function (config)
    {
        let event = window.event || arguments.callee.caller.arguments[0]; // 获取event对象
        event.stopPropagation();
        let input = event.currentTarget;
        // 初始化已选年月日
        initDate(input, config);
        // 生成DOM
        let datedom = createDom();
        // 显示
        showDateBox(datedom);
        // 绑定事件
        dateboxJQ = $('.' + dateboxCls).eq(0);
        bindEventForShow();
    }

    // 初始化:已选年月,保存日期框的INPUT的JQ对象引用
    let initDate = function (input, config)
    {
        // input的JQ对象
        inputJQ = $(input);

        // 用inpupt的值初始化时间,为空则默认今天时间.input时间格式只支持 yyyy-MM-dd HH:mm:ss(时间,秒部分可省略)
        let inputval = $.trim(input.value);
        if (/^[0-9]{4}-[0-9]{2}-[0-9]{2}$/.test(inputval))
        {
            inputval = inputval + ' 00:00:00';
        } else if (/^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}$/.test(inputval))
        {
            inputval = inputval + ':00';
        }
        else if (/^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$/.test(inputval))
        { }
        else
        {
            inputval = null;
        }
        // console.log(inputval);
        // 不带时间部分的日期串,用parse解后,会有时差.
        let inputDate = Date.parse(inputval);
        let date = isNaN(inputDate) ? new Date((new Date()).setHours(0, 0, 0)) : new Date(inputDate);
        //
        //console.log(date);
        cfg = {};
        cfg.year = date.getFullYear();
        cfg.month = date.getMonth();
        cfg.day = date.getDate();
        cfg.hour = date.getHours();
        cfg.minute = date.getMinutes();
        cfg.second = date.getSeconds();
        // 显示格式为日期('yyyy-MM-dd'),或者日期和时间('yyyy-MM-dd HH:mm:ss')
        cfg.dateFmt = 'yyyy-MM-dd';
        cfg.fmtType = 1;
        if (config && config.fmt == 'datetime')
        {
            cfg.dateFmt = 'yyyy-MM-dd HH:mm:ss';
            cfg.fmtType = 2;
        }
    }
    // 显示日期框
    let showDateBox = function (datedom)
    {
        //console.log(datedom);
        // 根据日期框的位置显示日期DOM框
        let thisleft = inputJQ.offset().left + 'px';
        let thistop = inputJQ.offset().top + inputJQ.outerHeight() + 'px';
        // 576px以下屏(手机屏) 显示在屏幕中央(css媒体查询设为固定定位了)
        let ww = $(window).width();
        if (ww < 576)
        {
            thisleft = 0;
            thistop = '25vh';
        }
        $('.' + dateboxCls).remove();
        // 显示新的日期框
        $('body').append(String.DataBind(datedom, { left: thisleft, top: thistop, exportName: exportName }));

        // 576以上屏,input框要能手动输入,焦点在input框.在手机上使用选择,不使用手输,焦点在日期控件上.
        if (ww < 576)
        {
            $('.' + dateboxCls).eq(0).focus();
        } else
        {

        }
    }
    //========================================================//
    // DOM生成
    //========================================================//
    // 生成整个日期框的DOM.并返回
    let createDom = function ()
    {
        let datebox = '<div class="date-box" style="left:${left};top:${top}" tabindex="-1">{0}{1}{2}{3}</div>';
        let ymtarea = String.Format('<div class="date-area-ymt">{0}{1}{2}</div>'
            , createDom_Year()
            , createDom_Month()
            , createDom_Today());

        let weekarea = String.Format('<div class="date-area-week">{0}</div>'
            , createDom_Week());

        let dayarea = String.Format('<div class="date-area-day">{0}</div>'
            , createDom_Day());
        // 时间区域
        let tcarea = '';
        if (cfg.fmtType == 2)
        {
            tcarea = String.Format('<div class="date-area-tc">{0}{1}{2}</div>'
                , createDom_Time()
                , createDom_Clear()
                , createDom_Ok());
        }
        return String.Format(datebox, ymtarea, weekarea, dayarea, tcarea);
    }

    // 1.生成年份区内容 前进,后退,年份 按钮
    let createDom_Year = function ()
    {
        let box = '<div class="date-area-year">{0}{1}{2}</div>';
        let prevbtn = '<a class="date-btn-prev">&lt;</a>';
        let yearbtn = String.Format(
            '<b class="date-btn-year" val="{0}">{0}年</b>'
            , cfg.year);
        let nextbtn = '<a class="date-btn-next">&gt;</a>';
        return String.Format(box, prevbtn, yearbtn, nextbtn);
    }

    // 1.1生成年份下拉选择框. selectedYear:可指定一个年份为已选定
    let createDom_YearSelect = function (selectedYear)
    {
        let ydoms = '';
        let ylist = domYear_Data();
        for (let i = 0; i < ylist.length; i++)
        {
            ydoms += String.Format('<b class="date-option-year {0}" val="{1}">{1}</b>',
                ylist[i] == selectedYear ? "selected" : "", ylist[i]);
        }
        return String.Format('<div class="date-select-year">{0}</div>', ydoms);
    }

    // 2.生成月份区 前进,后退,月份 按钮
    let createDom_Month = function ()
    {
        let box = '<div class="date-area-month">{0}{1}{2}</div>';
        let prevbtn = '<a class="date-btn-prev">&lt;</a>';
        let monthbtn = String.Format('<b class="date-btn-month" val="{0}">{1}月</b>'
            , cfg.month, cfg.month + 1);
        let nextbtn = '<a class="date-btn-next">&gt;</a>';
        return String.Format(box, prevbtn, monthbtn, nextbtn);
    }

    // 2.1生成月份下拉选择框. selectedMonth:可指定一个月份为已选定
    let createDom_MonthSelect = function (selectedMonth)
    {
        let mdoms = '';
        for (let i = 0; i < 12; i++)
        {
            mdoms += String.Format(
                '<b class="date-option-month {0}" val="{1}">{2}</b>'
                , selectedMonth == i ? "selected" : '', i, i + 1);
        }
        return String.Format('<div class="date-select-month">{0}</div>', mdoms);
    }

    // 3.生成星期标题头
    let createDom_Week = function ()
    {
        let weeksdom = '';
        let weeks = ['日', '一', '二', '三', '四', '五', '六'];
        for (let i = 0; i < weeks.length; i++)
        {
            weeksdom += String.Format('<b class="date-item-week{0}">{1}</b>'
                , i == 0 || i == 6 ? ' date-item-weekend' : '', weeks[i]);
        }
        return weeksdom;
    }

    // 4.生成天选项 daylist:日数据.不传则使用选定年月计算出日
    let createDom_Day = function (daylist)
    {
        let data = daylist || domDay_Data();
        let daydoms = '';
        for (var i = 0; i < data.length; i++)
        {
            let json = data[i];
            let daydom = '<b class="date-item-day${istoday}${isdayinmonth}${isselected}${isweekend}" year="${yyyy}" month="${MM}" day="${dd}">${dd}</b>';
            json.istoday = json.Istoday ? ' date-item-today' : '';
            json.isselected = json.Isselected ? ' selected' : '';
            json.isdayinmonth = json.Isdayinmonth ? '' : ' date-item-dayoutmonth';
            json.isweekend = json.Isweekend ? ' date-item-weekend' : '';
            json.exportName = exportName;
            daydoms += String.DataBind(daydom, json);
        }
        return daydoms;
    }
    // 5.生成时分秒区域
    let createDom_Time = function ()
    {
        let box = '<div class="date-area-time">{0}{1}{2}</div>';
        let hour = String.Format('<b class="date-btn-time date-btn-hour">{0}</b>:', cfg.hour);
        let minute = String.Format('<b class="date-btn-time date-btn-minute">{0}</b>:',cfg.minute);
        let second = String.Format('<b class="date-btn-time date-btn-second">{0}</b>', cfg.second);
        return String.Format(box, hour, minute, second);
    }
    // 5.1生成小时选择框
    let createDom_HourSelect = function ()
    {
        let doms = '';
        for (let i = 0; i < 24; i++)
        {
            doms += String.Format(
                '<b class="date-option-hour" val="{0}">{0}</b>', i);
        }
        return String.Format('<div class="date-select-hour">{0}</div>', doms);
    }
    // 5.2生成分钟,秒钟选择框
    let createDom_MinuteSelect = function ()
    {
        let doms = '';
        for (let i = 0; i < 60; i++)
        {
            doms += String.Format(
                '<b class="date-option-minute" val="{0}">{0}</b>', i);
        }
        return String.Format('<div class="date-select-minute">{0}</div>', doms);
    }
    // 5.3生成秒钟选择框
    let createDom_SecondSelect = function ()
    {
        let doms = '';
        for (let i = 0; i < 60; i++)
        {
            doms += String.Format('<b class="date-option-second" val="{0}">{0}</b>', i);
        }
        return String.Format('<div class="date-select-second">{0}</div>', doms);
    }
    // 6.生成今天按钮区域
    let createDom_Today = function ()
    {
        return '<div class="date-area-today"><a class="date-btn-today">今天</a></div>';
    }
    // 7.生成清除按钮区域
    let createDom_Clear = function ()
    {
        let box = '<div class="date-area-clear">{0}</div>';
        return String.Format(box, '<a class="date-btn-clear">清空</a>');
    }
    // 8.生成确定按钮区域 
    let createDom_Ok = function ()
    {
        let box = '<div class="date-area-ok">{0}</div>';
        return String.Format(box, '<a class="date-btn-ok">确定</a>');
    }

    // 根据选定的年,月刷新日(用于当在日期框上操作年,月等会改变年月的动作时)
    // yyyy:指定年,mm:指定月 daysdom:日的父级DOM的JQ对象(.daysrows)
    let resetDaysDom = function (yyyy, mm)
    {
        // 计算出指定年月的日数据
        let dayslist = domDay_Data(yyyy, mm);
        // 生成天DOM
        let daysdom = createDom_Day(dayslist);
        // 更新天DOM
        dateboxJQ.find('.date-area-day').html(daysdom);
        // 事件绑定
        bindEventForDaySelected();
    }

    //=================================================//
    //    为DOM提供的数据,年份 日
    //=================================================//
    // 根据已选年计算年份选项
    let domYear_Data = function ()
    {
        // 年份选择范围固定在[1900-2100]
        let data = [];
        for (let i = 1900; i < 2101; i++)
        {
            data.push(i);
        }
        return data;
    }

    // 根据已选年月或者传入指定年月,计算日的起始和结束
    // 日(天)总共六行七列42个,含已选年月所有日, 前推至最近的周日, 后推至最近或次近的周六
    let domDay_Data = function (yyyy, mm)
    {
        // 指定年 超范围则设为当天年
        let seledY = $.isNumeric(yyyy) ? parseInt(yyyy) : cfg.year;
        // 指定月 超范围设为当天月
        let seledM = $.isNumeric(mm) ? parseInt(mm) : cfg.month;

        // 指定年月的起止日(1~xx号)
        let startDay = new Date(seledY, seledM, 1);
        //let endDay = new Date(seledY, seledM + 1, 0);

        // 日期起点为指定年月的1号前推到最近的周日,终点为该月最后一天后推到最近的周六
        startDay.setDate(1 - startDay.getDay());
        //endDay.setDate(endDay.getDate() + (6 - endDay.getDay()));
        // 当天日期
        let todaystr = (new Date()).ToString('yyyyMMdd');
        let daylist = [];
        for (let i = 0; i < 42; i++)
        {
            let json = {};
            json.yyyy = startDay.getFullYear();
            json.MM = startDay.getMonth();
            json.dd = startDay.getDate();
            // 日是否属于指定年月中的日
            json.Isdayinmonth = json.MM == seledM;
            // 日是否为今天 
            json.Istoday = startDay.ToString('yyyyMMdd') == todaystr;
            // 日是否选定(等于文本框中已选日)
            json.Isselected =
                (json.yyyy == cfg.year && json.MM == cfg.month
                    && json.dd == cfg.day);
            // 这天是否为周六日(这里未真正判断,而是根据位置判断,每七天为一行,行首周日行尾周六)
            json.Isweekend = (i % 7 == 0 || (i + 1) % 7 == 0);
            //
            startDay.setDate(json.dd + 1);
            daylist.push(json);
        }
        //console.log(daylist);
        return daylist;
    }

    //===============================================================//
    //    事件方法:年,月的前进后退按钮,年月选择按钮,今天按钮
    //===============================================================//
    // 控件显示后,要绑定控件的基础事件.
    let bindEventForShow = function ()
    {
        bindEventForDateBox();
        bindEventForYearBtn();
        bindEventForMonthBtn();
        bindEventForYearMonthPrevNext();
        bindEventForTodayBtn();
        bindEventForHourBtn();
        bindEventForMinBtn();
        bindEventForSecBtn();
        bindEventForDaySelected();
        bindEventForClearBtn();
        bindEventForOkBtn();
    }

    let bindEventForDateBox = function ()
    {
        // 点击日期控件以内区域,阻止冒泡到根
        dateboxJQ.on('click', function (event)
        {
            event.stopPropagation();
            // 点击空白位置时,关闭已经打开的年,月,日,时,分,秒的选择框.需要在子元素上取消冒泡
            $(this).find('[class^=date-select]').remove();
        })
    }
    let bindEventForYearBtn = function ()
    {
        // 点击年按钮 显示年选择框
        dateboxJQ.find('.date-btn-year').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let seledY = $(thisobj).attr('val');
            // 年份选择框 .date-select-year
            let yearopsbox = $(thisobj).parent().find('.date-select-year');
            // 如果已经显示则关闭
            if (yearopsbox.length == 1)
            {
                yearopsbox.remove(); return;
            }
            // 先关闭其它弹出窗
            dateboxJQ.find('[class^=date-select]').remove();
            // 生成年份选择框,填充到年份选择框中
            $(thisobj).parent().append(createDom_YearSelect(seledY));
            // 定位已选年份到滚动框的中间(视口可见范围内)
            let yopsbox = $(thisobj).parent().find('.date-select-year');
            let yseled = yopsbox.find('.selected');
            if (yseled.length == 0)
                yseled = yopsbox.find('[val=' + (new Date()).getFullYear() + ']');
            // 计算这个年份选项离父框的TOP值,然后滚动条滚动这个值-父框高/2
            let scrollval = yseled.position().top - yopsbox.height() / 2;
            yopsbox.scrollTop(scrollval);
            // 绑定年份选择点击事件
            bindEventForYearSelected();
        })
    }
    let bindEventForMonthBtn = function ()
    {
        // 点击月按钮 显示月选择框
        dateboxJQ.find('.date-btn-month').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let seledM = $(thisobj).attr('val');
            let monthsops = $(thisobj).parent().find('.date-select-month');
            // 如果已经显示则关闭
            if (monthsops.length == 1)
            {
                monthsops.remove(); return;
            }
            // 先关闭其它弹出窗
            dateboxJQ.find('[class^=date-select]').remove();
            $(thisobj).parent().append(createDom_MonthSelect(seledM));
            // 绑定月分选项点击事件
            bindEventForMonthSelected();
        })
    }
    let bindEventForYearSelected = function ()
    {
        // 点击年份选项 选定一个年份 
        dateboxJQ.find('.date-option-year').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            // 
            // 所选年份值
            let y = $(thisobj).attr('val');
            // 更新年份按钮显示值
            dateboxJQ.find('.date-btn-year').attr('val', y).html(y + '年');
            // 关闭年份选择框
            $(thisobj).parent().remove();
            // 刷新 日
            let m = dateboxJQ.find('.date-btn-month').attr('val');
            resetDaysDom(y, m);
        })
    }
    let bindEventForMonthSelected = function ()
    {
        // 点击月份选项 选定一个月份
        dateboxJQ.find('.date-option-month').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            // 
            // 所选月份值
            let m = parseInt($(thisobj).attr('val'));
            dateboxJQ.find('.date-btn-month').attr('val', m).html((m + 1) + '月');
            // 关闭月份选择框
            $(thisobj).parent().remove();
            // 刷新 日
            let y = dateboxJQ.find('.date-btn-year').attr('val');
            resetDaysDom(y, m);
        })
    }
    let bindEventForYearMonthPrevNext = function ()
    {
        // 点击年份,月份的前进和后退按钮 btntype:1=年按钮,2=月按钮. dir:1=前进,2=后退
        dateboxJQ.find('.date-btn-prev,.date-btn-next').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let btntype = $(thisobj).parent().hasClass('date-area-year') ? 1 : 2;
            let dir = $(thisobj).hasClass('date-btn-next') ? 1 : 2;
            //
            let ybtn = dateboxJQ.find('.date-btn-year');
            let mbtn = dateboxJQ.find('.date-btn-month');
            let y = parseInt(ybtn.attr('val'));
            let m = parseInt(mbtn.attr('val'));
            // 计算并刷新年或月按钮值 年份前进后退值[1-9999]
            if (btntype == 1)
            {
                y = dir == 1 ? y + 1 : y - 1;
                if (y < 1) y = 9999;
                else if (y > 9999) y = 1;
            }
            else if (btntype == 2)
            {
                m = dir == 1 ? m + 1 : m - 1;
                if (m < 0)
                {
                    m = 11;
                    // 年往后退一年,如果为1年,则不变
                    if (y > 1)
                        y = y - 1;
                }
                else if (m > 11)
                {
                    m = 0;
                    // 年往前进一年,如果为9999年,则不变
                    if (y < 9999)
                        y = y + 1;
                }
            }
            ybtn.attr('val', y).html(y + '年');
            mbtn.attr('val', m).html((m + 1) + '月');
            // 刷新日
            //console.log(y+'----'+m);
            resetDaysDom(y, m);
        })
    }
    let bindEventForTodayBtn = function ()
    {
        // 点击今天按钮 设置今天日期到input框
        dateboxJQ.find('.date-btn-today').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let today = new Date(new Date().toLocaleDateString());
            inputJQ.val(today.ToString(cfg.dateFmt));
            //
            mydate.close();
        })
    }
    let bindEventForHourBtn = function ()
    {
        // 点击小时按钮 显示小时选择框
        dateboxJQ.find('.date-btn-hour').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let hourselecct = $(thisobj).parent().find('.date-select-hour');
            // 点击时分秒下拉框按钮时,先取消其按钮的打开样式,打开后,再给自己加上打开样式
            $(thisobj).parent().find('.date-btn-time').removeClass('open');
            // 如果已经显示则关闭
            if (hourselecct.length == 1)
            {
                hourselecct.remove(); return;
            }
            // 先关闭其它弹出窗
            dateboxJQ.find('[class^=date-select]').remove();
            $(thisobj).parent().append(createDom_HourSelect());
            $(thisobj).addClass('open');
            // 绑定小时选项点击事件
            bindEventForHourSelected();
        })
    }
    let bindEventForMinBtn = function ()
    {
        // 点击分钟按钮 显示分钟选择框
        dateboxJQ.find('.date-btn-minute').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let minselecct = $(thisobj).parent().find('.date-select-minute');
            // 点击时分秒下拉框按钮时,先取消其按钮的打开样式,打开后,再给自己加上打开样式
            $(thisobj).parent().find('.date-btn-time').removeClass('open');
            // 如果已经显示则关闭
            if (minselecct.length == 1)
            {
                minselecct.remove(); return;
            }
            // 先关闭其它弹出窗
            dateboxJQ.find('[class^=date-select]').remove();
            $(thisobj).parent().append(createDom_MinuteSelect());
            $(thisobj).addClass('open');
            // 绑定分钟选项点击事件
            bindEventForMinSelected();
        })
    }
    let bindEventForSecBtn = function ()
    {
        // 点击秒钟按钮 显示秒钟选择框
        dateboxJQ.find('.date-btn-second').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let secselecct = $(thisobj).parent().find('.date-select-second');
            // 点击时分秒下拉框按钮时,先取消其按钮的打开样式,打开后,再给自己加上打开样式
            $(thisobj).parent().find('.date-btn-time').removeClass('open');
            // 如果已经显示则关闭
            if (secselecct.length == 1)
            {
                secselecct.remove(); return;
            }
            // 先关闭其它弹出窗
            dateboxJQ.find('[class^=date-select]').remove();
            $(thisobj).parent().append(createDom_SecondSelect());
            $(thisobj).addClass('open');
            // 绑定秒钟选项点击事件
            bindEventForSecSelected();
        })
    }
    let bindEventForHourSelected = function ()
    {
        // 选择小时 修改小时按钮显示值
        dateboxJQ.find('.date-option-hour').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let h = $(thisobj).attr('val');
            dateboxJQ.find('.date-btn-hour').html(h);
            cfg.hour = h;
            //
            $(thisobj).parent().remove();
        })
    }
    let bindEventForMinSelected = function ()
    {
        // 选择分钟 修改按钮显示值
        dateboxJQ.find('.date-option-minute').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let m = $(thisobj).attr('val');
            dateboxJQ.find('.date-btn-minute').html(m);
            cfg.minute = m;
            //
            $(thisobj).parent().remove();
        })
    }
    let bindEventForSecSelected = function ()
    {
        // 选择秒钟 修改按钮显示值
        dateboxJQ.find('.date-option-second').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let s = $(thisobj).attr('val');
            dateboxJQ.find('.date-btn-second').html(s);
            cfg.second = s;
            //
            $(thisobj).parent().remove();
        })
    }
    let bindEventForDaySelected = function ()
    {
        // 选择天 设置这天日期到Input框
        dateboxJQ.find('.date-item-day').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            let date = new Date($(thisobj).attr('year'), $(thisobj).attr('month')
                , $(thisobj).attr('day'),cfg.hour,cfg.minute,cfg.second);
            inputJQ.val(date.ToString(cfg.dateFmt));
            //
            mydate.close();
        })
    }
    let bindEventForClearBtn = function ()
    {
        // 点击清空
        dateboxJQ.find('.date-btn-clear').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            inputJQ.val('');
            mydate.close();
        })
    }
    let bindEventForOkBtn = function ()
    {
        // 点击确定按钮
        dateboxJQ.find('.date-btn-ok').on('click', function (event)
        {
            event.stopPropagation();
            let thisobj = event.currentTarget;
            //
            // 找到选中的日 设置到Input框 如果没有选中的日,使用今天
            let seledDay = dateboxJQ.find('.date-item-day.selected');
            let inputVal = seledDay.length == 0
                ? new Date(new Date().toLocaleDateString(), cfg.hour, cfg.minute, cfg.second)
                : new Date(seledDay.attr('year'), seledDay.attr('month'), seledDay.attr('day')
                    , cfg.hour, cfg.minute, cfg.second);
            
            inputJQ.val(inputVal.ToString(cfg.dateFmt));
            //
            mydate.close();
        })
    }

    // 关闭日期框
    mydate.close = function ()
    {
        dateboxJQ = null;
        inputJQ = null;
        cfg = null;
        $('.' + dateboxCls).remove();
    }

    // 点击日期控件以外区域,关闭控件. 
    $(document).click(function ()
    {
        mydate.close();
    })
    //
    window[exportName] = mydate;
})();
