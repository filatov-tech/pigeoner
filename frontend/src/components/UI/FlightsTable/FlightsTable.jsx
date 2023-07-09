import React, {useMemo} from 'react';
import { MaterialReactTable } from "material-react-table";
import {MRT_Localization_RU} from "material-react-table/locales/ru";
import {Link} from "react-router-dom";

const FlightsTable = ({data}) => {
    const addLinkToCell = function () {
        return ({cell, row}) => {
            return <>{cell.getValue()}<Link to={`/flights/${row.original.id}`}
                                            className="table-link"></Link></>;
        };
    }

    const columns = useMemo(() => [
            {
                accessorKey: 'locationName',
                header: 'Место выпуска',
                Cell: addLinkToCell()
            },
            {
                accessorKey: 'distance',
                header: 'Дистанция, км',
                Cell: addLinkToCell()
            },
            {
                accessorKey: 'numberParticipants',
                header: 'Мои голуби',
                Cell: addLinkToCell()
            },
            {
                accessorKey: 'myPassed',
                header: 'В зачете',
                Cell: addLinkToCell()
            },
            {
                accessorKey: 'totalParticipants',
                header: 'Всего участников',
                Cell: addLinkToCell()
            },
            {
                accessorKey: 'departure',
                header: 'Выпуск',
                Cell: addLinkToCell()
            }
        ],
        []
    );

    return <MaterialReactTable
        columns={columns}
        data={data}
        muiTablePaperProps={{
            sx: {
                borderRadius: '0.5rem',
            },
        }}
        muiTableBodyCellProps={{
            sx: {
                position: "relative"
            }
        }}
        localization={MRT_Localization_RU}
    />;
};

export default FlightsTable;