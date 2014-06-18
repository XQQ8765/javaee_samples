/*!
 * Copyright 2014 Dell Software Group.
 * ALL RIGHTS RESERVED.
 * Version 6.0.3
 */
'use strict';

(function() {

var namespace = window.dellapm = {};

/*
if (!window.XMLHttpRequest) {
	var doNothing = function() {};
	var returnFalse = function() { return false; };
	//empty implementation for logger
	namespace.Log = {
		trace: doNothing,
		debug: doNothing,
		info: doNothing,
		warn: doNothing,
		error: doNothing,
		fatal: doNothing,
		isTraceEnabled: returnFalse,
		isDebugEnabled: returnFalse,
		isInfoEnabled: returnFalse,
		isWarnEnabled: returnFalse,
		isErrorEnabled: returnFalse,
		isFatalEnabled: returnFalse
	};
	return;
}
*/

// Configuration options
var logLevel = 3; // the log level (info)
var debugToConsole = true;

// -----------------------

// Private code
if (!Date.now) {
	Date.now = function() { return new Date().getTime(); }
}

// TODO this should be the debug window
if (debugToConsole && !window.console) {
	window.console = {
		_window: open('', 'instrumentationLog', 'width=800,height=300,location=no,toolbar=no'),

		log: function(message) {
			var debugWindow = this._window;
			if (debugWindow && !debugWindow.closed) {
				var debugDocument = debugWindow.document;
				var div = debugDocument.createElement('div');
				div.appendChild(debugDocument.createTextNode(message));
				debugDocument.body.appendChild(div);
				debugDocument.documentElement.scrollTop = debugDocument.body.scrollHeight;
			}
		}
	}
}


var Config = {
    _configurationRead: false,
    _archiverProxyHost: undefined,
    _archiverProxyHostSecure: undefined,
    _requestUUID: undefined,

    readConfigFromHtml: function() {
        // TODO direct inject as script to avoid dom access, or at least give HTML id= to prevent looping
        var metas = document.getElementsByTagName('meta');
        for (var i=0; i<metas.length; i++) {
            var metaTagName = metas[i].getAttribute("name");
            if (metaTagName == "FoglightAPHost") {
                this._archiverProxyHost = metas[i].getAttribute("content");
                if (this._archiverProxyHost && this._archiverProxyHost.indexOf(':') == -1) {
                    this._archiverProxyHost += ":7630";
                }
            } else if (metaTagName == "FoglightAPHostSecure") {
                this._archiverProxyHostSecure = metas[i].getAttribute("content");
                if (this._archiverProxyHostSecure && this._archiverProxyHostSecure.indexOf(':') == -1) {
                    this._archiverProxyHostSecure += ":7631";
                }
            } else if (metaTagName == "FoglightRequestUUID") {
                this._requestUUID = metas[i].getAttribute("content");
            }
        }
        this._configurationRead = true;
        if (debugToConsole) {
            if (!this._archiverProxyHost) {
                console.log('WARNING: FoglightAPHost not found in HTML response');
            }
            if (!this._archiverProxyHostSecure) {
                console.log('WARNING: FoglightAPHostSecure not found in HTML response');
            }
            if (!this._requestUUID) {
                console.log('WARNING: FoglightRequestUUID not found in HTML response');
            }
        }
    },

    getArchiverProxyHost: function() {
        if (!this._configurationRead) {
            this.readConfigFromHtml();
        }
        return this._archiverProxyHost;
    },

    getArchiverProxyHostSecure: function() {
        if (!this._configurationRead) {
            this.readConfigFromHtml();
        }
        return this._archiverProxyHostSecure;
    },

    getRequestUUID: function() {
        if (!this._configurationRead) {
            this.readConfigFromHtml();
        }
        return this._requestUUID;
    },

    isSecureRequest: function() {
        return ('https:' == document.location.protocol);
    }

};

var Data = {
    _numFields: 0, // TODO better way to get count?
	_result: {},

	setHitProperty: function(name, value) {
        this._result[name] = value;
        if (!this._result.hasOwnProperty(name)) {
            this._numFields++;
        }
		if (debugToConsole) {
			console.log('setHitProperty():' + name + ': ' + value);
		}
	},

    getResultAsJSON: function() {
        var sb = [];
        sb[sb.length] = '[\n\t{\n\t\t"hitProperties":';
        sb[sb.length] = JSON.stringify(this._result);
        sb[sb.length] = ',\n\t},\n]';
        return sb.join("");
    },

	flush: function(sync) {
		try {
            var archiverProxyHost = Config.isSecureRequest() ? Config.getArchiverProxyHostSecure() : Config.getArchiverProxyHost();
            if (archiverProxyHost) {
                var archiverProxyURL = document.location.protocol + '//' + archiverProxyHost + '/archiverProxy?op=uploadhitdata';
                var r = new XMLHttpRequest();
                r.open('post', archiverProxyURL, !sync);
                r.setRequestHeader('Content-Type', 'text/plain');
                r.send(this.getResultAsJSON());
                if (debugToConsole) {
                    console.log('flush():' + this.getResultAsJSON());
                }
            } else if (debugToConsole) {
                console.log('flush(): ERROR: Could not send to ArchiverProxy; ArchiverProxy host configuration not found in HTML?');
            }
		}
		catch (r) {
			//ignore if browser does not support XMLHttpRequest
            if (debugToConsole) {
                console.log('flush(): ERROR:' + r.message);
            }
		}
        // Reset buffers
        this._result = {};
        this._numFields = 0;
	},

	finalFlush: function() {
		if (this._numFields != 0) {
			this.flush(true);
		}
	}
};

/*
var Log = {
	levels: ['fatal', 'error', 'warn', 'info', 'debug', 'trace'],

	log: function(level, message, e) {
		if (level <= logLevel) {
			var fields = [];
			fields.push(Log.levels[level]);
			fields.push(message);
			if (e) {
				fields.push(e.name);
				fields.push(e.message);
				if (e.stack) {
					fields.push(e.stack);
				}
			}
			Data.setHitProperty('log', fields);
		}
	}
};

namespace.Log = {
	trace: function(message, e) {
		Log.log(5, message, e);
	},

	debug: function(message, e) {
		Log.log(4, message, e);
	},

	info: function(message, e) {
		Log.log(3, message, e);
	},

	warn: function(message, e) {
		Log.log(2, message, e);
	},

	error: function(message, e) {
		Log.log(1, message, e);
	},

	fatal: function(message, e) {
		Log.log(0, message, e);
	},

	isTraceEnabled: function() {
		return logLevel >= 5;
	},

	isDebugEnabled: function() {
		return logLevel >= 4;
	},

	isInfoEnabled: function() {
		return logLevel >= 3;
	},

	isWarnEnabled: function() {
		return logLevel >= 2;
	},

	isErrorEnabled: function() {
		return logLevel >= 1;
	},

	isFatalEnabled: function() {
		return logLevel >= 0;
	}
};
Data.setHitProperty('initial', [document.URL, window.innerWidth, window.innerHeight]);

window.onerror = function(message, url, line) {
	Data.setHitProperty('error', [url, line, message]);
	return false;
};
*/

var GlobalHandlers = {

    load: function() {
		var performanceFlag = '2';
		if (window.performance) {
			performanceFlag = '1';
            Data.setHitProperty('isInstrumentationHit', true);
            Data.setHitProperty('requestUUID', Config.getRequestUUID());
			var navigation = performance.navigation;
			if (navigation) {
				Data.setHitProperty('performanceNavigation', [navigation.type, navigation.redirectCount]);
			}
			var timing = performance.timing;
			if (timing) {
                Data.setHitProperty('performanceTiming', [
						timing.navigationStart,
						timing.unloadEventStart,
						timing.unloadEventEnd,
						timing.redirectStart,
						timing.redirectEnd,
						timing.fetchStart,
						timing.domainLookupStart,
						timing.domainLookupEnd,
						timing.connectStart,
						timing.connectEnd,
						timing.secureConnectionStart,
						timing.requestStart,
						timing.responseStart,
						timing.responseEnd,
						timing.domLoading,
						timing.domInteractive,
						timing.domContentLoadedEventStart,
						timing.domContentLoadedEventEnd,
						timing.domComplete,
						timing.loadEventStart,
						timing.loadEventEnd
				]);
			}
		}
		Data.flush(false);
	},

	unload: function() {
		Data.finalFlush();
	}
};

if (window.addEventListener) {
	window.addEventListener('load', function() {
		setTimeout(GlobalHandlers.load, 0)
	}, false);
	window.addEventListener('unload', GlobalHandlers.unload, false);
}

})();
