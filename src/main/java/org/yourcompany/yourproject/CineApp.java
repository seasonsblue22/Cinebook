
package org.yourcompany.yourproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CineApp
{
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args)
    {
        try (Connection con = DBConnection.getConnection())
        {
            System.out.println("Connected to CineBook database!");

            while (true)
            {
                System.out.println("\n===== CINEBOOK =====");
                System.out.println("1. View Shows");
                System.out.println("2. View Seat Map");
                System.out.println("3. Book Tickets");
                System.out.println("4. Cancel Booking");
                System.out.println("5. Booking History");
                System.out.println("6. Exit");
                System.out.print("Choice: ");

                int ch = readInt();

                try
                {
                    if (ch == 1)
                        showShows(con);
                    else if (ch == 2)
                        viewSeats(con);
                    else if (ch == 3)
                        bookTickets(con);
                    else if (ch == 4)
                        cancelBooking(con);
                    else if (ch == 5)
                        history(con);
                    else if (ch == 6)
                        break;
                    else
                        System.out.println("Invalid choice!");
                }
                catch (SQLException e)
                {
                    System.out.println("Database error: " + e.getMessage());
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed: " + e.getMessage());
        }

        System.out.println("Thank you for using CineBook!");
    }

    static int readInt()
    {
        try
        {
            return Integer.parseInt(sc.nextLine().trim());
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    static void showShows(Connection con) throws SQLException
    {
        String sql = "SELECT * FROM shows ORDER BY show_id";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql))
        {
            System.out.println("\nAVAILABLE SHOWS");

            while (rs.next())
            {
                System.out.println(
                    rs.getInt("show_id") + ". " +
                    rs.getString("movie_name") + " - " +
                    rs.getString("show_time"));
            }
        }
    }

    static int chooseShow(Connection con) throws SQLException
    {
        showShows(con);
        System.out.print("Enter show ID: ");
        int id = readInt();

        try (PreparedStatement ps = con.prepareStatement(
                "SELECT show_id FROM shows WHERE show_id = ?"))
        {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                    return id;
            }
        }

        System.out.println("Invalid show ID!");
        return -1;
    }

    static void displaySeats(Connection con, int showId)
        throws SQLException
    {
        boolean[][] booked = new boolean[5][8];

        String sql =
            "SELECT row_label, seat_no, is_booked FROM seats " +
            "WHERE show_id = ? ORDER BY row_label, seat_no";

        try (PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, showId);

            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    int r = rs.getString("row_label").charAt(0) - 'A';
                    int c = rs.getInt("seat_no") - 1;

                    booked[r][c] = rs.getBoolean("is_booked");
                }
            }
        }

        System.out.println("\n       1 2 3 4 5 6 7 8");

        for (int r = 0; r < 5; r++)
        {
            System.out.print((char)('A' + r) + "      ");

            for (int c = 0; c < 8; c++)
            {
                System.out.print(booked[r][c] ? "X " : "O ");
            }

            System.out.println();
        }

        System.out.println("O = Available, X = Booked");
        System.out.println("Regular A-B: Rs.150");
        System.out.println("Premium C-D: Rs.250");
        System.out.println("VIP E: Rs.400");
    }

    static void viewSeats(Connection con) throws SQLException
    {
        int showId = chooseShow(con);

        if (showId != -1)
            displaySeats(con, showId);
    }

    static class SeatType
    {
        enum Type
        {
            REGULAR(150),
            PREMIUM(250),
            VIP(400);

            final double price;

            Type(double price)
            {
                this.price = price;
            }

            double getPrice()
            {
                return price;
            }
        }

        static Type getType(String row)
        {
            if (row.equals("A") || row.equals("B"))
                return Type.REGULAR;

            if (row.equals("C") || row.equals("D"))
                return Type.PREMIUM;

            return Type.VIP;
        }
    }

    static class Booking
    {
        int id;
        int showId;
        double total;
        List<String> seats;

        Booking(int id, int showId, double total,
                List<String> seats)
        {
            this.id = id;
            this.showId = showId;
            this.total = total;
            this.seats = seats;
        }
    }

    static double calculateTotal(double amount)
    {
        return amount;
    }

    static double calculateTotal(double amount, String coupon)
    {
        if (coupon.equalsIgnoreCase("CINE10"))
            return amount * 0.90;

        return amount;
    }

    static void bookTickets(Connection con) throws SQLException
    {
        int showId = chooseShow(con);

        if (showId == -1)
            return;

        displaySeats(con, showId);

        System.out.print("Enter seats separated by commas (e.g. A1,A2): ");
        String input = sc.nextLine().trim().toUpperCase();

        String[] arr = input.split(",");
        LinkedHashSet<String> selected = new LinkedHashSet<>();

        for (String s : arr)
        {
            s = s.trim();

            if (!s.matches("[A-E][1-8]") || !selected.add(s))
            {
                System.out.println("Invalid or duplicate seat: " + s);
                return;
            }
        }

        if (selected.isEmpty())
        {
            System.out.println("No seats selected!");
            return;
        }

        System.out.print("Coupon code (Enter to skip): ");
        String coupon = sc.nextLine().trim();

        con.setAutoCommit(false);

        try
        {
            double total = 0;
            Map<String, Integer> seatIds = new LinkedHashMap<>();
            Map<String, Double> prices = new LinkedHashMap<>();

            for (String seat : selected)
            {
                String row = seat.substring(0, 1);
                int num = Integer.parseInt(seat.substring(1));

                String sql =
                    "SELECT seat_id, is_booked FROM seats " +
                    "WHERE show_id = ? AND row_label = ? AND seat_no = ? " +
                    "FOR UPDATE";

                try (PreparedStatement ps = con.prepareStatement(sql))
                {
                    ps.setInt(1, showId);
                    ps.setString(2, row);
                    ps.setInt(3, num);

                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (!rs.next() || rs.getBoolean("is_booked"))
                        {
                            throw new SQLException(
                                "Seat " + seat +
                                " is unavailable. No seats were booked.");
                        }

                        seatIds.put(seat, rs.getInt("seat_id"));
                    }
                }

                double price =
                    SeatType.getType(row).getPrice();

                prices.put(seat, price);
                total += price;
            }

            double finalTotal = coupon.isEmpty()
                ? calculateTotal(total)
                : calculateTotal(total, coupon);

            String bookingSql =
                "INSERT INTO bookings " +
                "(show_id, total_amount, coupon_code) VALUES (?, ?, ?)";

            int bookingId;

            try (PreparedStatement ps = con.prepareStatement(
                    bookingSql, Statement.RETURN_GENERATED_KEYS))
            {
                ps.setInt(1, showId);
                ps.setDouble(2, finalTotal);
                ps.setString(3, coupon.isEmpty() ? null : coupon);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys())
                {
                    if (!rs.next())
                        throw new SQLException("Could not create booking.");

                    bookingId = rs.getInt(1);
                }
            }

            for (String seat : selected)
            {
                String update =
                    "UPDATE seats SET is_booked = TRUE " +
                    "WHERE seat_id = ? AND is_booked = FALSE";

                try (PreparedStatement ps = con.prepareStatement(update))
                {
                    ps.setInt(1, seatIds.get(seat));

                    if (ps.executeUpdate() != 1)
                        throw new SQLException(
                            "Seat " + seat + " could not be booked.");
                }

                String insert =
                    "INSERT INTO booking_seats " +
                    "(booking_id, seat_id, price) VALUES (?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(insert))
                {
                    ps.setInt(1, bookingId);
                    ps.setInt(2, seatIds.get(seat));
                    ps.setDouble(3, prices.get(seat));
                    ps.executeUpdate();
                }
            }

            con.commit();

            System.out.println("\n===== BOOKING CONFIRMED =====");
            System.out.println("Booking ID: BK" + (1000 + bookingId));
            System.out.println("Seats: " + String.join(", ", selected));
            System.out.printf("Original total: Rs. %.2f%n", total);

            if (!coupon.isEmpty() &&
                coupon.equalsIgnoreCase("CINE10"))
                System.out.println("Coupon: CINE10 (10% discount)");
            else if (!coupon.isEmpty())
                System.out.println("Coupon not recognized; no discount applied.");

            System.out.printf("Amount payable: Rs. %.2f%n", finalTotal);
        }
        catch (SQLException e)
        {
            con.rollback();
            System.out.println(e.getMessage());
        }
        finally
        {
            con.setAutoCommit(true);
        }
    }

    static void cancelBooking(Connection con) throws SQLException
    {
        System.out.print("Enter booking ID (e.g. BK1001): ");
        String input = sc.nextLine().trim().toUpperCase();

        if (!input.matches("BK[0-9]+"))
        {
            System.out.println("Invalid booking ID!");
            return;
        }

        int id;

        try
        {
            id = Integer.parseInt(input.substring(2)) - 1000;
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid booking ID!");
            return;
        }

        if (id <= 0)
        {
            System.out.println("Invalid booking ID!");
            return;
        }

        con.setAutoCommit(false);

        try
        {
            double total;
            int showId;

            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT total_amount, show_id, status " +
                    "FROM bookings WHERE booking_id = ? FOR UPDATE"))
            {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery())
                {
                    if (!rs.next())
                        throw new SQLException("Booking not found!");

                    if (!rs.getString("status").equals("CONFIRMED"))
                        throw new SQLException("Booking already cancelled!");

                    total = rs.getDouble("total_amount");
                    showId = rs.getInt("show_id");
                }
            }

            String release =
                "UPDATE seats s JOIN booking_seats bs " +
                "ON s.seat_id = bs.seat_id " +
                "SET s.is_booked = FALSE WHERE bs.booking_id = ?";

            try (PreparedStatement ps = con.prepareStatement(release))
            {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE bookings SET status = 'CANCELLED' " +
                    "WHERE booking_id = ?"))
            {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            con.commit();

            System.out.println("Booking cancelled successfully!");
            System.out.printf("Refund (80%%): Rs. %.2f%n", total * 0.80);
            System.out.printf("Non-refundable amount: Rs. %.2f%n",
                              total * 0.20);
        }
        catch (SQLException e)
        {
            con.rollback();
            System.out.println(e.getMessage());
        }
        finally
        {
            con.setAutoCommit(true);
        }
    }

    static void history(Connection con) throws SQLException
    {
        String sql =
            "SELECT b.booking_id, s.movie_name, s.show_time, " +
            "b.total_amount, b.status, b.coupon_code " +
            "FROM bookings b JOIN shows s ON b.show_id = s.show_id " +
            "ORDER BY b.booking_id DESC";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql))
        {
            System.out.println("\n===== BOOKING HISTORY =====");

            while (rs.next())
            {
                System.out.println(
                    "Booking: BK" + (1000 + rs.getInt("booking_id")) +
                    " | " + rs.getString("movie_name") +
                    " | " + rs.getString("show_time") +
                    " | Rs. " + rs.getBigDecimal("total_amount") +
                    " | " + rs.getString("status") +
                    " | Coupon: " + rs.getString("coupon_code"));
            }
        }
    }
}
