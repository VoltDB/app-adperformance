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

import java.math.BigDecimal;
//import org.voltdb.ProcInfo;
import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
//import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;
import org.voltdb.types.TimestampType;

public class TrackEvent extends VoltProcedure {

    public final SQLStmt selectCreative = new SQLStmt(
        "SELECT campaign_id, advertiser_id FROM creatives WHERE creative_id = ?;");

    public final SQLStmt selectInventory = new SQLStmt(
        "SELECT site_id, page_id FROM inventory WHERE inventory_id = ?;");

    public final SQLStmt insertEvent = new SQLStmt(
        "INSERT INTO event_data VALUES (" +
        "?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?" +
        ");");

    public long run( TimestampType utc_time,
                     long ip_address,
                     long cookie_uid,
                     int creative_id,
                     int inventory_id,
                     int type_id,
                     BigDecimal cost
		     ) throws VoltAbortException {

        // derive counter fields from type_id
        //   0 = impression
        //   1 = clickthrough
        //   2 = conversion
        int is_impression = (type_id == 0) ? 1 : 0;
        int is_clickthrough = (type_id == 1) ? 1 : 0;
        int is_conversion = (type_id == 2) ? 1 : 0;

        // lookup creative_id and inventory_id
        voltQueueSQL(selectCreative, creative_id);
        voltQueueSQL(selectInventory, inventory_id);
        VoltTable lookups[] = voltExecuteSQL();

        VoltTableRow creative = lookups[0].fetchRow(0);
        int campaign_id = (int)creative.getLong(0);
        int advertiser_id = (int)creative.getLong(1);

        VoltTableRow inventory = lookups[1].fetchRow(0);
        int site_id = (int)inventory.getLong(0);
        int page_id = (int)inventory.getLong(1);

	voltQueueSQL( insertEvent,
                      utc_time,
                      ip_address,
                      cookie_uid,
                      creative_id,
                      inventory_id,
                      type_id,
                      cost,
                      campaign_id,
                      advertiser_id,
                      site_id,
                      page_id,
                      is_impression,
                      is_clickthrough,
                      is_conversion
                      );

        voltExecuteSQL();

	return ClientResponse.SUCCESS;
	
    }
}
