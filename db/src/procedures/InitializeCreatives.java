/* This file is part of VoltDB.
 * Copyright (C) 2008-2014 VoltDB Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with VoltDB.  If not, see <http://www.gnu.org/licenses/>.
 */
package procedures;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;

public class InitializeCreatives extends VoltProcedure {
    public final SQLStmt insert = new SQLStmt(
        "INSERT INTO creatives VALUES (?,?,?);"
        );

    public long run(int advertisers, int campaigns, int creatives)
        throws VoltAbortException {
        long creativeMaxID = 0;
        for (int advertiser=1; advertiser<=advertisers; advertiser++) {
            for (int campaign=1; campaign<=campaigns; campaign++) {
                for (int i=1; i<=creatives; i++) {
                    creativeMaxID++;
                    voltQueueSQL(insert, creativeMaxID, campaign, advertiser);
                }
            }
        }

        voltExecuteSQL(true);

        return 0;
    }
}
